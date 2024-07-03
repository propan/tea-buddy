package com.github.propan.teabuddy.repository;

import com.github.propan.teabuddy.models.Crawler;

import java.util.Optional;
import java.util.UUID;

public interface CrawlerRepository {

    void registerCrawler(String storeName, String crawlerClass);

    void disableCrawler(UUID id);

    Optional<Crawler> findExecutableCrawler();

}
