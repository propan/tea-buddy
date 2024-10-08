/*
 * This file is generated by jOOQ.
 */
package com.github.propan.teabuddy.repository.jooq.tables.records;


import com.github.propan.teabuddy.repository.jooq.tables.Crawlers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jooq.JSON;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class CrawlersRecord extends UpdatableRecordImpl<CrawlersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>PUBLIC.CRAWLERS.ID</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.CRAWLERS.ID</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>PUBLIC.CRAWLERS.NAME</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.CRAWLERS.NAME</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>PUBLIC.CRAWLERS.ENABLED</code>.
     */
    public void setEnabled(Boolean value) {
        set(2, value);
    }

    /**
     * Getter for <code>PUBLIC.CRAWLERS.ENABLED</code>.
     */
    public Boolean getEnabled() {
        return (Boolean) get(2);
    }

    /**
     * Setter for <code>PUBLIC.CRAWLERS.LAST_CRAWLED_AT</code>.
     */
    public void setLastCrawledAt(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>PUBLIC.CRAWLERS.LAST_CRAWLED_AT</code>.
     */
    public LocalDateTime getLastCrawledAt() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>PUBLIC.CRAWLERS.LAST_RESULT</code>.
     */
    public void setLastResult(JSON value) {
        set(4, value);
    }

    /**
     * Getter for <code>PUBLIC.CRAWLERS.LAST_RESULT</code>.
     */
    public JSON getLastResult() {
        return (JSON) get(4);
    }

    /**
     * Setter for <code>PUBLIC.CRAWLERS.NEXT_CRAWL_AT</code>.
     */
    public void setNextCrawlAt(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>PUBLIC.CRAWLERS.NEXT_CRAWL_AT</code>.
     */
    public LocalDateTime getNextCrawlAt() {
        return (LocalDateTime) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CrawlersRecord
     */
    public CrawlersRecord() {
        super(Crawlers.CRAWLERS);
    }

    /**
     * Create a detached, initialised CrawlersRecord
     */
    public CrawlersRecord(UUID id, String name, Boolean enabled, LocalDateTime lastCrawledAt, JSON lastResult, LocalDateTime nextCrawlAt) {
        super(Crawlers.CRAWLERS);

        setId(id);
        setName(name);
        setEnabled(enabled);
        setLastCrawledAt(lastCrawledAt);
        setLastResult(lastResult);
        setNextCrawlAt(nextCrawlAt);
        resetChangedOnNotNull();
    }
}
