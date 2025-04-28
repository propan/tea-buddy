package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.*;
import com.github.propan.teabuddy.parsers.DataProcessingException;
import com.github.propan.teabuddy.parsers.StoreParser;
import com.github.propan.teabuddy.repository.CrawlerRepository;
import com.github.propan.teabuddy.repository.ItemsRepository;
import com.github.propan.teabuddy.utils.BaseTestCase;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CrawlerServiceTest extends BaseTestCase {

    @Mock
    private StoreParser parser;
    @Mock
    private CrawlerRepository crawlerRepository;
    @Mock
    private ItemsRepository itemRepository;
    @Mock
    private HttpClient httpClient;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CrawlerService crawlerService;

    @Mock
    private HttpResponse<String> httpResponse;

    @BeforeEach
    public void init() {
        crawlerService = new CrawlerService(List.of(parser), crawlerRepository, itemRepository, httpClient, notificationService);
    }

    @AfterEach
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(crawlerRepository, itemRepository, httpClient, notificationService);
    }

    @Test
    public void testInit() {
        when(parser.getStoreName()).thenReturn("test-store");

        crawlerService.init();

        verify(crawlerRepository, times(1)).registerCrawler("test-store", parser.getClass().getName());
    }

    @Nested
    class FindNextTask {

        @Test
        public void noCrawlerFound() {
            when(crawlerRepository.findExecutableCrawler()).thenReturn(Optional.empty());

            Optional<TaskMeta> result = crawlerService.findNextTask();
            assertThat(result).isEmpty();

            verify(crawlerRepository, times(1)).findExecutableCrawler();
        }

        @Test
        public void noParserFound() {
            UUID crawlerId = UUID.randomUUID();

            when(crawlerRepository.findExecutableCrawler()).thenReturn(Optional.of(new Crawler(crawlerId, "unknown-parser")));

            Optional<TaskMeta> result = crawlerService.findNextTask();
            assertThat(result).isEmpty();

            verify(crawlerRepository, times(1)).findExecutableCrawler();
            verify(crawlerRepository, times(1)).disableCrawler(crawlerId);
        }

        @Test
        public void success() {
            UUID crawlerId = UUID.randomUUID();

            when(crawlerRepository.findExecutableCrawler()).thenReturn(Optional.of(new Crawler(crawlerId, parser.getClass().getName())));

            Optional<TaskMeta> result = crawlerService.findNextTask();
            assertThat(result).isNotEmpty();

            assertThat(result.get().crawlerId()).isEqualTo(crawlerId);
            assertThat(result.get().parser()).isEqualTo(parser);

            verify(crawlerRepository, times(1)).findExecutableCrawler();
        }
    }

    @Nested
    class CrawlStore {

        @Test
        public void fetchProductsFails() throws IOException, InterruptedException {
            UUID crawlerId = UUID.randomUUID();

            DataProcessingException exception = new DataProcessingException("test");

            when(crawlerRepository.findExecutableCrawler()).thenReturn(Optional.of(new Crawler(crawlerId, parser.getClass().getName())));

            when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
            when(httpResponse.body()).thenReturn("test-body");
            when(parser.getStorePages()).thenReturn(Stream.of("https://test.com"));
            when(parser.getStoreName()).thenReturn("test-store");
            when(parser.parse("test-body")).thenThrow(exception);
            when(parser.getStoreName()).thenReturn("test-store");

            doNothing().when(notificationService).sendErrorNotification(exception);

            crawlerService.crawlStore();

            verify(crawlerRepository, times(1)).findExecutableCrawler();
            verify(httpClient, times(1)).send(any(), eq(HttpResponse.BodyHandlers.ofString()));
            verify(crawlerRepository, times(1)).writeCrawlingResult(crawlerId, Crawler.ExecutionResult.from(exception));
            verify(notificationService, times(1)).sendErrorNotification(exception);
        }

        @Test
        public void successfulExecution() throws IOException, InterruptedException {
            UUID crawlerId = UUID.randomUUID();

            List<StoreListItem> items = List.of(
                    new StoreListItem(
                            Store.WHITE2TEA,
                            "test-vendor",
                            "test-title",
                            ItemType.BLACK_TEA,
                            "test-url",
                            "test-image",
                            "10$"
                    )
            );

            when(crawlerRepository.findExecutableCrawler()).thenReturn(Optional.of(new Crawler(crawlerId, parser.getClass().getName())));

            when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
            when(httpResponse.body()).thenReturn("test-body");
            when(parser.getStorePages()).thenReturn(Stream.of("http://test.com"));
            when(parser.getStoreName()).thenReturn("test-store");
            when(parser.parse("test-body")).thenReturn(items);

            when(itemRepository.storeItems(items)).thenReturn(1);

            crawlerService.crawlStore();

            verify(crawlerRepository, times(1)).findExecutableCrawler();
            verify(httpClient, times(1)).send(any(), eq(HttpResponse.BodyHandlers.ofString()));
            verify(itemRepository, times(1)).storeItems(items);
            verify(crawlerRepository, times(1)).writeCrawlingResult(crawlerId, Crawler.ExecutionResult.success("Found %d items", 1));
        }

    }

}
