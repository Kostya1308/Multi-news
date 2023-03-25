package by.clevertec.repository;

import by.clevertec.entity.News;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class NewsRepositoryIntegrationTest {
    @Autowired
    private NewsRepository newsRepository;
    private final PageRequest pageRequest = PageRequest.of(0, 100);


    @Test
    void findAllByTitleJavaContainsOnlyTwoItemsTest() {
        Page<News> page = newsRepository.findAllByTitleContainsPageable("Java", pageRequest);

        assertEquals(2, page.get().count());
    }

    @Test
    void findAllByDateTimeCreateGreaterThanNowReturnEmptyPageTest() {
        Page<News> page = newsRepository.findAllByDateTimeCreateGreaterThanPageable(LocalDateTime.now(), pageRequest);

        assertFalse(page.get().findAny().isPresent());
    }

    @Test
    public void findByIdLoadCommentsLazilyAndThrowsExceptionTest(){
        Optional<News> news = newsRepository.findById(1L);

        news.ifPresent(item -> assertThrows(LazyInitializationException.class, () -> item.getComments().size()));
    }


    @Test
    public void findByIdWithCommentsLoadCommentsEagerAndDoesNotThrowsExceptionTest(){
        Optional<News> news = newsRepository.findByIdWithComments(1L);

        news.ifPresent(item -> assertDoesNotThrow(() -> item.getComments().size()));
    }
}