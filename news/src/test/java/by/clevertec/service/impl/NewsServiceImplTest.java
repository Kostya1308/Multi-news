package by.clevertec.service.impl;

import by.clevertec.cache.Cache;
import by.clevertec.entity.News;
import by.clevertec.factory.NewsFactory;
import by.clevertec.repository.NewsRepository;
import extension.LoggingExtension;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(LoggingExtension.class)
@ExtendWith(MockitoExtension.class)
public class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private Cache<Long, News> cache;
    @InjectMocks
    private NewsServiceImpl newsService;
    private Logger logger;

    @Test
    @DisplayName("Should return News after save")
    void givenNews_whenSave_thenReturnNewsId() {
        NewsFactory newsFactory = new NewsFactory();
        News news = (News) newsFactory.create();

        Mockito.when(newsRepository.save(news)).thenReturn(news);
        Assertions.assertEquals(news, newsService.save(news));
    }

    @Test
    @DisplayName("Should return news after getById")
    void whenGetById_theReturnNews() {
        NewsFactory newsFactory = new NewsFactory();
        News news = (News) newsFactory.create();

        Mockito.when(newsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(news));

        Assertions.assertEquals(news, newsService.getById(Mockito.anyLong()).orElse(new News()));
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}