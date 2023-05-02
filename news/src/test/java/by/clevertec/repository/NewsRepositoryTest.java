package by.clevertec.repository;

import by.clevertec.entity.News;
import by.clevertec.factory.NewsFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class NewsRepositoryTest {
    @Autowired
    TestEntityManager entityManager;
    @Autowired
    NewsRepository newsRepository;

    @Test
    @DisplayName("Should return news after findById")
    void whenFindById_thenReturnNews() {
        NewsFactory newsFactory = new NewsFactory();
        News news = (News) newsFactory.create();

        entityManager.persist(news);

        newsRepository.findById(1L);
    }
}