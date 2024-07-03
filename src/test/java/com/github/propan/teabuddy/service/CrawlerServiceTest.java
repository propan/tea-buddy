package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.parsers.StoreParser;
import com.github.propan.teabuddy.parsers.White2TeaParser;
import com.github.propan.teabuddy.repository.CrawlerRepository;
import com.github.propan.teabuddy.utils.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

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

    @Test
    public void testInit() {
        when(parser.getStoreName()).thenReturn("test-store");

        crawlerService.init();

        verify(repository, times(1)).registerCrawler("test-store", parser.getClass().getName());
    }
}
