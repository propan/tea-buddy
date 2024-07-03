package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.parsers.StoreParser;
import com.github.propan.teabuddy.repository.CrawlerRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            repository.registerCrawler(parser.getStoreName(), parser.getClass().getName());
        }
    }
}
