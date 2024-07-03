package com.github.propan.teabuddy.repository;

import com.github.propan.teabuddy.parsers.White2TeaParser;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.jdbc.Sql;

@JooqTest(
        properties = {
                "spring.test.database.replace=none",
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver",
        }
)
@Sql("classpath:db/migration/V1__init_db.sql")
class CrawlerRepositoryImplTest {

    @Autowired
    private DSLContext dsl;

    private CrawlerRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CrawlerRepositoryImpl(dsl);
    }

    @Test
    void shouldRegisterCrawler() {
        repository.registerCrawler("testStore", White2TeaParser.class.getName());
    }
}
