/*
 * This file is generated by jOOQ.
 */
package com.github.propan.teabuddy.repository.jooq.tables.records;


import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.repository.jooq.tables.Items;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ItemsRecord extends UpdatableRecordImpl<ItemsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>PUBLIC.ITEMS.ID</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEMS.ID</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>PUBLIC.ITEMS.STORE</code>.
     */
    public void setStore(Store value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEMS.STORE</code>.
     */
    public Store getStore() {
        return (Store) get(1);
    }

    /**
     * Setter for <code>PUBLIC.ITEMS.VENDOR</code>.
     */
    public void setVendor(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEMS.VENDOR</code>.
     */
    public String getVendor() {
        return (String) get(2);
    }

    /**
     * Setter for <code>PUBLIC.ITEMS.TITLE</code>.
     */
    public void setTitle(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEMS.TITLE</code>.
     */
    public String getTitle() {
        return (String) get(3);
    }

    /**
     * Setter for <code>PUBLIC.ITEMS.TYPE</code>.
     */
    public void setType(ItemType value) {
        set(4, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEMS.TYPE</code>.
     */
    public ItemType getType() {
        return (ItemType) get(4);
    }

    /**
     * Setter for <code>PUBLIC.ITEMS.SOURCE_URL</code>.
     */
    public void setSourceUrl(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEMS.SOURCE_URL</code>.
     */
    public String getSourceUrl() {
        return (String) get(5);
    }

    /**
     * Setter for <code>PUBLIC.ITEMS.IMAGE_URL</code>.
     */
    public void setImageUrl(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEMS.IMAGE_URL</code>.
     */
    public String getImageUrl() {
        return (String) get(6);
    }

    /**
     * Setter for <code>PUBLIC.ITEMS.PRICE</code>.
     */
    public void setPrice(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEMS.PRICE</code>.
     */
    public String getPrice() {
        return (String) get(7);
    }

    /**
     * Setter for <code>PUBLIC.ITEMS.CREATED_AT</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(8, value);
    }

    /**
     * Getter for <code>PUBLIC.ITEMS.CREATED_AT</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(8);
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
     * Create a detached ItemsRecord
     */
    public ItemsRecord() {
        super(Items.ITEMS);
    }

    /**
     * Create a detached, initialised ItemsRecord
     */
    public ItemsRecord(UUID id, Store store, String vendor, String title, ItemType type, String sourceUrl, String imageUrl, String price, LocalDateTime createdAt) {
        super(Items.ITEMS);

        setId(id);
        setStore(store);
        setVendor(vendor);
        setTitle(title);
        setType(type);
        setSourceUrl(sourceUrl);
        setImageUrl(imageUrl);
        setPrice(price);
        setCreatedAt(createdAt);
        resetChangedOnNotNull();
    }
}
