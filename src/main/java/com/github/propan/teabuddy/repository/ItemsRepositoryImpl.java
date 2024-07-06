package com.github.propan.teabuddy.repository;

import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.models.StoreListItem;
import com.github.propan.teabuddy.repository.jooq.tables.records.ItemsRecord;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.github.propan.teabuddy.repository.jooq.Tables.ITEMS;

@Repository
public class ItemsRepositoryImpl implements ItemsRepository {

    private final DSLContext context;

    @Autowired
    public ItemsRepositoryImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public int storeItems(List<StoreListItem> items) {
        InsertValuesStep8<ItemsRecord, UUID, Store, String, String, ItemType, String, String, String> insert = context.insertInto(ITEMS)
                .columns(
                        ITEMS.ID,
                        ITEMS.STORE,
                        ITEMS.VENDOR,
                        ITEMS.TITLE,
                        ITEMS.TYPE,
                        ITEMS.SOURCE_URL,
                        ITEMS.IMAGE_URL,
                        ITEMS.PRICE
                );

        for (StoreListItem item : items) {
            insert = insert.values(
                    item.id(),
                    item.store(),
                    item.vendor(),
                    item.title(),
                    item.type(),
                    item.sourceUrl(),
                    item.imageUrl(),
                    item.price()
            );
        }
        return insert.onDuplicateKeyIgnore().execute();
    }

    @Override
    public List<StoreListItem> findNewItemsSince(LocalDateTime ts) {
        return context.selectFrom(ITEMS)
                .where(ITEMS.CREATED_AT.gt(ts))
                .fetchStream()
                .map(record -> new StoreListItem(
                        record.getStore(),
                        record.getVendor(),
                        record.getTitle(),
                        record.getType(),
                        record.getSourceUrl(),
                        record.getImageUrl(),
                        record.getPrice()
                ))
                .toList();
    }
}
