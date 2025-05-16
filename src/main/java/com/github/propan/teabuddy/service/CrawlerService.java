package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.Crawler;
import com.github.propan.teabuddy.models.StoreListItem;
import com.github.propan.teabuddy.models.TaskMeta;
import com.github.propan.teabuddy.parsers.StoreParser;
import com.github.propan.teabuddy.repository.CrawlerRepository;
import com.github.propan.teabuddy.repository.ItemsRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CrawlerService {

    private static final Logger log = LoggerFactory.getLogger(CrawlerService.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static final int MAX_CRAWL_DEPTH = 5;

    private final Map<String, Instant> lastErrorTimestamp = new ConcurrentHashMap<>();
    private static final Duration ERROR_NOTIFICATION_COOLDOWN = Duration.ofHours(8);

    private final List<StoreParser> parsers;
    private final CrawlerRepository crawlerRepository;
    private final ItemsRepository itemsRepository;
    private final NotificationService notificationService;
    private final HttpClient httpClient;

    @Autowired
    public CrawlerService(List<StoreParser> parsers, CrawlerRepository crawlerRepository, ItemsRepository itemsRepository, HttpClient httpClient, NotificationService notificationService) {
        this.parsers = parsers;
        this.crawlerRepository = crawlerRepository;
        this.itemsRepository = itemsRepository;
        this.notificationService = notificationService;
        this.httpClient = httpClient;
    }

    @PostConstruct
    public void init() {
        for (StoreParser parser : parsers) {
            log.info("Registering store crawler: {} => {}", parser.getStoreName(), parser.getClass().getName());
            this.crawlerRepository.registerCrawler(parser.getStoreName(), parser.getClass().getName());
        }
    }

    @Scheduled(fixedRate = 60, timeUnit = TimeUnit.SECONDS)
    public void crawlStore() {
        this.findNextTask().ifPresent(taskMeta -> {
            StoreParser parser = taskMeta.parser();

            log.debug("Crawling store: {}", parser.getStoreName());

            try {
                int newItemsTotal = 0;
                for (String pageUrl : parser.getStorePages().limit(MAX_CRAWL_DEPTH).toList()) {
                    log.info("Crawling store page: {}", pageUrl);

                    List<StoreListItem> products = this.fetchProducts(parser, pageUrl);

                    int newItemsCount = this.itemsRepository.storeItems(products);
                    newItemsTotal += newItemsCount;

                    log.info("Found {} items on page {}, of which {} are new ", products.size(), pageUrl, newItemsCount);

                    if (newItemsCount < products.size()) {
                        // there are known items on the page, we can stop crawling
                        break;
                    }
                }

                log.debug("Crawling finished, found {} new items", newItemsTotal);

                this.crawlerRepository.writeCrawlingResult(
                        taskMeta.crawlerId(), Crawler.ExecutionResult.success("Found %d items", newItemsTotal)
                );
            } catch (Exception e) {
                log.error(String.format("Failed to fetch products from %s", parser.getStoreName()), e);
                this.crawlerRepository.writeCrawlingResult(taskMeta.crawlerId(), Crawler.ExecutionResult.from(e));

                if (shouldSendErrorNotification(parser.getStoreName())) {
                    this.notificationService.sendErrorNotification(e);
                }
            }
        });
    }

    protected List<StoreListItem> fetchProducts(StoreParser parser, String storeUrl) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(storeUrl))
                .timeout(Duration.ofSeconds(5))
                .GET();

        parser.getHeaders().forEach(builder::header);

        return parser.parse(
                this.httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString()).body()
        );
    }

    protected Optional<TaskMeta> findNextTask() {
        Optional<Crawler> result = this.crawlerRepository.findExecutableCrawler();
        if (result.isEmpty()) {
            return Optional.empty();
        }

        Crawler crawler = result.get();

        Optional<StoreParser> parser = this.parsers.stream()
                .filter(p -> p.getClass().getName().equals(crawler.className()))
                .findFirst();

        if (parser.isEmpty()) {
            log.error("Failed to resolve parser for crawler: {}", crawler.className());
            this.crawlerRepository.disableCrawler(crawler.id());
            return Optional.empty();
        }

        return Optional.of(new TaskMeta(crawler.id(), parser.get()));
    }

    private boolean shouldSendErrorNotification(String storeName) {
        Instant now = Instant.now();
        Instant lastNotification = lastErrorTimestamp.getOrDefault(storeName, Instant.MIN);

        if (now.minus(ERROR_NOTIFICATION_COOLDOWN).isAfter(lastNotification)) {
            lastErrorTimestamp.put(storeName, now);
            return true;
        }

        return false;
    }
}
