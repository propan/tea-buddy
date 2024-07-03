package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.Crawler;
import com.github.propan.teabuddy.models.TaskMeta;
import com.github.propan.teabuddy.parsers.StoreParser;
import com.github.propan.teabuddy.repository.CrawlerRepository;
import com.github.propan.teabuddy.utils.BaseTestCase;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CrawlerServiceTest extends BaseTestCase {

    @Mock
    private StoreParser parser;
    @Mock
    private CrawlerRepository repository;

    @InjectMocks
    private CrawlerService crawlerService;

    @BeforeEach
    public void init() {
        crawlerService = new CrawlerService(List.of(parser), repository);
    }

    @AfterEach
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    public void testInit() {
        when(parser.getStoreName()).thenReturn("test-store");

        crawlerService.init();

        verify(repository, times(1)).registerCrawler("test-store", parser.getClass().getName());
    }

    @Nested
    class FindNextTask {

        @Test
        public void noCrawlerFound() {
            when(repository.findExecutableCrawler()).thenReturn(Optional.empty());

            Optional<TaskMeta> result = crawlerService.findNextTask();
            assertThat(result).isEmpty();

            verify(repository, times(1)).findExecutableCrawler();
        }

        @Test
        public void noParserFound() {
            UUID crawlerId = UUID.randomUUID();

            when(repository.findExecutableCrawler()).thenReturn(Optional.of(new Crawler(crawlerId, "unknown-parser")));

            Optional<TaskMeta> result = crawlerService.findNextTask();
            assertThat(result).isEmpty();

            verify(repository, times(1)).findExecutableCrawler();
            verify(repository, times(1)).disableCrawler(crawlerId);
        }

        @Test
        public void success() {
            UUID crawlerId = UUID.randomUUID();

            when(repository.findExecutableCrawler()).thenReturn(Optional.of(new Crawler(crawlerId, parser.getClass().getName())));

            Optional<TaskMeta> result = crawlerService.findNextTask();
            assertThat(result).isNotEmpty();

            assertThat(result.get().crawlerId()).isEqualTo(crawlerId);
            assertThat(result.get().parser()).isEqualTo(parser);

            verify(repository, times(1)).findExecutableCrawler();
        }
    }

}
