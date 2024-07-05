package com.github.propan.teabuddy.repository;

import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.models.StoreListItem;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JooqTest(
        properties = {
                "spring.test.database.replace=none",
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver",
        }
)
@Sql("classpath:db/migration/V1__init_db.sql")
class ItemsRepositoryImplTest {

    @Autowired
    private DSLContext dsl;

    private ItemsRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ItemsRepositoryImpl(dsl);
    }

    @Test
    void storeItems() {
        StoreListItem item1 = new StoreListItem(
                Store.YUNNAN_SOURCING,
                "Yunnan Sourcing Tea Shop",
                "Hand-Made Flowering Black Tea Cones from Feng Qing - 1 Kilogram / Spring 2024",
                ItemType.BLACK_TEA,
                "https://yunnansourcing.com/collections/new-products/products/hand-made-flowering-black-tea-cones-from-feng-qing",
                "https://yunnansourcing.com/cdn/shop/products/thumb1_140d5732-84c6-47ee-bfaa-ddf4dab0f9f8_512x510.jpg?v=1560801157",
                "109.00$"
        );
        StoreListItem item2 = new StoreListItem(
                Store.WHITE2TEA,
                "white2tea",
                "2024 Tihkal - 200g",
                ItemType.RAW_PUER_TEA,
                "https://white2tea.com/collections/latest-additions/products/2024-tihkal",
                "https://white2tea.com/cdn/shop/files/2024TikhalrawPuer-2_1600x.jpg?v=1716449118",
                "113.95$"
        );
        StoreListItem item3 = new StoreListItem(
                Store.WHITE2TEA,
                "white2tea",
                "2024 Penetralia - 200g",
                ItemType.BLACK_TEA,
                "https://white2tea.com/collections/latest-additions/products/2024-penetralia",
                "https://white2tea.com/cdn/shop/files/2024PenetraliaSunDriedBlackTea_1600x.jpg?v=1716446912",
                "60.95$"
        );
        assertThat(repository.storeItems(List.of(item1, item2))).isEqualTo(2);
        // consecutive inserts should ignore duplicates
        assertThat(repository.storeItems(List.of(item1, item2))).isEqualTo(0);
        // only new item should be inserted
        assertThat(repository.storeItems(List.of(item1, item2, item3))).isEqualTo(1);
    }
}
