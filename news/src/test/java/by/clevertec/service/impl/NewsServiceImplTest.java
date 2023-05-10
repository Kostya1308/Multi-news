package by.clevertec.service.impl;

import by.clevertec.cache.Cache;
import by.clevertec.entity.News;
import by.clevertec.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests of the NewsServiceImpl")
class NewsServiceImplTest {
    @Mock
    NewsRepository newsRepository;
    @Mock
    Cache<Long, News> cache;
    @InjectMocks
    NewsServiceImpl newsService;
    private News news;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        news = News.builder().id(1L)
                .title("title")
                .text("text")
                .comments(Set.of())
                .build();

        pageable = PageRequest.of(1, 10);
    }

    @Test
    @DisplayName("After saving should return news")
    void givenNews_whenSave_thenReturnNewsObject() {
        Mockito.when(newsRepository.save(news)).thenReturn(news);
        Mockito.when(cache.put(news.getId(), news)).thenReturn(true);

        assertNotNull(newsService.save(news));
    }

    @Test
    @DisplayName("After getting by id should return news object")
    void whenGetById_thenReturnNewsObject() {
        Mockito.when(cache.get(1L)).thenReturn(Optional.of(news));
        assertNotNull(newsService.getById(1L));
    }

    @Test
    @DisplayName("After getting by id with comments should return news object")
    void whenGetByIdWithComments_thenReturnNewsObjectWithComments() {
        Mockito.when(cache.get(1L)).thenReturn(Optional.of(news));
        assertNotNull(newsService.getByIdWithComments(1L));
        assertNotNull(newsService.getByIdWithComments(1L).orElse(new News()).getComments());
    }

    @Test
    @DisplayName("After getting all should return page object")
    void whenGetAll_thenReturnPageObject() {
        Mockito.when(newsRepository.findAll(pageable)).thenReturn(Page.empty());
        assertNotNull(newsService.getAll(pageable));
    }


    @Test
    @DisplayName("After getting all by title should return page object")
    void whenGetAllByTitleContainsPageable_thenReturnPageObject() {
        Mockito.when(newsRepository.findAllByTitleContainsPageable("Title", pageable)).thenReturn(Page.empty());
        assertNotNull(newsService.getAllByTitleContainsPageable("Title", pageable));
    }
}