package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.Crawler;
import com.github.propan.teabuddy.models.TaskMeta;
import com.github.propan.teabuddy.parsers.StoreParser;
import com.github.propan.teabuddy.repository.CrawlerRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrawlerService {

    private static final Logger log = LoggerFactory.getLogger(CrawlerService.class);

    private final List<StoreParser> parsers;
    private final CrawlerRepository repository;

    @Autowired
    public CrawlerService(List<StoreParser> parsers, CrawlerRepository repository) {
        this.parsers = parsers;
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        for (StoreParser parser : parsers) {
            log.info("Registering store crawler: {} => {}", parser.getStoreName(), parser.getClass().getName());
            this.repository.registerCrawler(parser.getStoreName(), parser.getClass().getName());
        }
    }

    public Optional<TaskMeta> findNextTask() {
        Optional<Crawler> result = this.repository.findExecutableCrawler();
        if (result.isEmpty()) {
            return Optional.empty();
        }

        Crawler crawler = result.get();

        Optional<StoreParser> parser = this.parsers.stream()
                .filter(p -> p.getClass().getName().equals(crawler.className()))
                .findFirst();

        if (parser.isEmpty()) {
            log.error("Failed to resolve parser for crawler: {}", crawler.className());
            this.repository.disableCrawler(crawler.id());
            return Optional.empty();
        }

        return Optional.of(new TaskMeta(crawler.id(), parser.get()));
    }

}
