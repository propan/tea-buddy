package com.github.propan.teabuddy.repository;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.github.propan.teabuddy.repository.jooq.Tables.CRAWLERS;

@Repository
public class CrawlerRepositoryImpl implements CrawlerRepository {

    private final DSLContext context;

    @Autowired
    public CrawlerRepositoryImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public void registerCrawler(String storeName, String crawlerClass) {
        this.context.insertInto(CRAWLERS)
                .set(CRAWLERS.ID, UUID.nameUUIDFromBytes(storeName.getBytes()))
                .set(CRAWLERS.NAME, crawlerClass)
                .set(CRAWLERS.ENABLED, Boolean.TRUE)
                .set(CRAWLERS.NEXT_CRAWL_AT, java.time.LocalDateTime.now())
                .onDuplicateKeyUpdate()
                .set(CRAWLERS.NAME, crawlerClass)
                .execute();
    }
}
