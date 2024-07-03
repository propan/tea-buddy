package com.github.propan.teabuddy.repository;

import com.github.propan.teabuddy.models.Crawler;
import com.github.propan.teabuddy.repository.jooq.tables.records.CrawlersRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static com.github.propan.teabuddy.repository.jooq.Tables.CRAWLERS;

@Repository
public class CrawlerRepositoryImpl implements CrawlerRepository {

    private final DSLContext context;
    private final Random random;

    private static final int MIN_CRAWLING_TIMEOUT_MINUTES = 1;
    private static final int CRAWL_JITTER_MINUTES = 1;

    @Autowired
    public CrawlerRepositoryImpl(DSLContext context) {
        this.context = context;
        this.random = new Random();
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

    @Override
    public Crawler findExecutableCrawler() {
        int nextCrawlAt = MIN_CRAWLING_TIMEOUT_MINUTES + random.nextInt(CRAWL_JITTER_MINUTES);

        CrawlersRecord result = this.context.update(CRAWLERS)
                .set(CRAWLERS.NEXT_CRAWL_AT, LocalDateTime.now().plusMinutes(nextCrawlAt))
                .where(CRAWLERS.ENABLED.eq(Boolean.TRUE))
                .and(CRAWLERS.NEXT_CRAWL_AT.le(LocalDateTime.now()))
                .limit(1)
                .returning()
                .fetchOne();

        if (result == null) {
            return null;
        } else {
            return new Crawler(result.getId(), result.getName());
        }
    }

    @Override
    public void disableCrawler(UUID id) {
        this.context.update(CRAWLERS)
                .set(CRAWLERS.ENABLED, Boolean.FALSE)
                .where(CRAWLERS.ID.eq(id))
                .execute();
    }
}
