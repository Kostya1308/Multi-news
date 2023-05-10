package by.clevertec.api;

import by.clevertec.dto.NewsDTO;
import by.clevertec.entity.News;
import by.clevertec.mapper.NewsMapper;
import by.clevertec.service.interfaces.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import extension.InvalidNewsDTOParameterResolver;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@DisplayName("Tests of the News API")
public class NewsApiTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    NewsService newsService;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    ObjectMapper objectMapper;

    private News news;
    private NewsDTO newsDTO;

    private static final DockerImageName POSTGRE_SQL_IMAGE = DockerImageName.parse("postgres");

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRE_SQL_IMAGE)
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

    static {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUp() {
        news = News.builder()
                .title("text_title")
                .text("test_text")
                .build();
        newsDTO = NewsDTO.builder()
                .id("1")
                .title("text_title")
                .text("test_text")
                .build();
    }

    @Nested
    @ExtendWith(InvalidNewsDTOParameterResolver.class)
    @DisplayName("Using invalid newsDTO without text-field")
    public class InvalidData {
        @Test
        @DisplayName("After saving invalid news returns 406-status")
        void givenInvalidNewsDTO_whenCreateNews_thenReturnStatusIsNotAcceptable(NewsDTO newsDTO) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(newsDTO)))
                    .andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        }

        @Test
        @DisplayName("After updating invalid news returns 406-status")
        void givenInvalidNewsDTO_whenUpdateNews_thenReturnStatusIsNotAcceptable(NewsDTO newsDTO) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", news.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(newsDTO)))
                    .andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        }
    }

    @Nested
    @DisplayName("Using the id of a non-existing news")
    public class NonExistingNews {
        @Test
        @DisplayName("After getting non-existing news by id returns 404-status")
        void givenId_whenGetNonExistingNews_thenReturnStatusBadRequest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/news/{id}", news.getId()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        @DisplayName("After updating non-existing news by id returns 404-status")
        void givenId_whenUpdateNonExistingNews_thenReturnStatusBadRequest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", news.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(newsDTO)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        @DisplayName("After deleting non-existing news by id returns 404-status")
        void givenId_whenDeleteNonExistingNews_thenReturnStatusBadRequest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", news.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(newsDTO)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Using the id of an existing news")
    public class ExistingNews {

        @Test
        @DisplayName("After getting news by id returns 200-status and newsDTO")
        void givenId_whenGetNews_thenReturnStatusOkAndNewsDTO() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/news/{id}", 1L))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @Transactional
        @DisplayName("After deleting news by id returns 200-status")
        void givenId_whenDeleteNews_thenReturnStatusOk() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(newsDTO)))
                    .andExpect(MockMvcResultMatchers.status().isOk());

        }
    }
}
