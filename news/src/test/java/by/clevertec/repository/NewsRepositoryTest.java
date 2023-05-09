//package by.clevertec.repository;
//
//import by.clevertec.entity.News;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import javax.sql.DataSource;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@Testcontainers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class NewsRepositoryTest {
//    @Autowired
//    private NewsRepository newsRepository;
//    @Autowired
//    private TestEntityManager entityManager;
//    @Autowired
//    DataSource dataSource;
//
//    private static final DockerImageName POSTGRESQL_IMAGE_NAME = DockerImageName.parse("postgres:9.6.12");
//
//    @Container
//    private static PostgreSQLContainer<?> postgreSQLContainer =
//            new PostgreSQLContainer<>(POSTGRESQL_IMAGE_NAME)
//                    .withDatabaseName("news-test")
//                    .withUsername("postgres")
//                    .withPassword("6%Itrlfkp765")
//                    .withExposedPorts(5432);
//
//    @DynamicPropertySource
//    static void setProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//    }
//
//    @Test
//    void test() {
//        News news = News.builder()
//                .text("TEST")
//                .title("TITLE")
//                .build();
//
//    }
//}