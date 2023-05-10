package by.clevertec.repository;

import by.clevertec.entity.News;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Tests of the NewsRepository")
class NewsRepositoryTest {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private DataSource dataSource;

    private News news;

    @BeforeEach
    void setUp() {
        news = News.builder()
                .text("test_text")
                .title("text_title")
                .build();
    }

    private static final DockerImageName POSTGRESQL_IMAGE_NAME = DockerImageName.parse("postgres:15.2-alpine");

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(POSTGRESQL_IMAGE_NAME)
                    .withDatabaseName("news-test")
                    .withUsername("postgres")
                    .withPassword("root")
                    .withExposedPorts(5432);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    @DisplayName("Should return list of the news that contain the title")
    void givenKeyTitle_whenFindAllByTitleContains_ReturnListNewsThatContainKeyTitle() {
        String key = "a";
        Page<News> page = newsRepository.findAllByTitleContainsPageable(key, Pageable.unpaged());
        page.get().forEach(itemNews -> assertTrue(itemNews.getTitle().contains(key)));
    }

    @Test
    @DisplayName("Should return list of the news with date-time of create less than now")
    void givenKeyDateTime_whenFindAllByDateTimeCreateLessThan_ReturnListNewsWithDateTimeCreateLessThanNow() {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        Page<News> page = newsRepository.findAllByDateTimeCreateLessThanPageable(dateTimeNow, Pageable.unpaged());
        page.get().forEach(itemNews -> assertTrue(itemNews.getDateTimeCreate().isBefore(dateTimeNow)));
    }

    @Test
    @DisplayName("Should return news with eager loaded comments")
    void givenId_whenFindByIdWithComments_ReturnNewsWithEagerLoadedComments() {
        Long id = 1L;
        news = newsRepository.findByIdWithComments(id).orElseGet(News::new);
        entityManager.clear();
        entityManager.getEntityManager().close();

        assertAll(
                () -> assertDoesNotThrow(() -> news.getComments()),
                () -> assertFalse(news.getComments().isEmpty())
        );
    }
}