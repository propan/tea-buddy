package com.github.propan.teabuddy.repository;

import com.github.propan.teabuddy.models.Crawler;
import com.github.propan.teabuddy.parsers.White2TeaParser;
import com.github.propan.teabuddy.repository.jooq.tables.records.CrawlersRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static com.github.propan.teabuddy.repository.jooq.Tables.CRAWLERS;
import static org.assertj.core.api.Assertions.assertThat;

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
        repository.registerCrawler("registerTest", White2TeaParser.class.getName());
    }

    @Test
    void shouldFindExecutableCrawler() {
        repository.registerCrawler("executableTest", White2TeaParser.class.getName());

        Optional<Crawler> crawler = repository.findExecutableCrawler();
        assertThat(crawler).isNotEmpty().hasValueSatisfying(c -> {
            assertThat(c.className()).isEqualTo(White2TeaParser.class.getName());
        });

        // consecutive call should return null
        assertThat(repository.findExecutableCrawler()).isEmpty();
    }

    @Test
    void shouldDisableCrawler() {
        repository.registerCrawler("disableTest", White2TeaParser.class.getName());

        CrawlersRecord record = dsl.selectFrom(CRAWLERS)
                .where(CRAWLERS.NAME.eq(White2TeaParser.class.getName()))
                .fetchOne();
        assertThat(record).isNotNull();

        repository.disableCrawler(record.getId());

        record = dsl.selectFrom(CRAWLERS)
                .where(CRAWLERS.NAME.eq(White2TeaParser.class.getName()))
                .fetchOne();
        assertThat(record).isNotNull();
        assertThat(record.getEnabled()).isFalse();

        // consecutive call should return null
        assertThat(repository.findExecutableCrawler()).isEmpty();
    }

}
