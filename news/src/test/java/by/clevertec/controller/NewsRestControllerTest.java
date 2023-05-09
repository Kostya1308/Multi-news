package by.clevertec.controller;

import by.clevertec.dto.NewsDTO;
import by.clevertec.entity.News;
import by.clevertec.exception.NewsNotFoundException;
import by.clevertec.mapper.CommentMapper;
import by.clevertec.mapper.NewsMapper;
import by.clevertec.service.interfaces.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import extension.InvalidNewsDTOParameterResolver;
import extension.ValidNewsDTOParameterResolver;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(NewsRestController.class)
@DisplayName("Tests of the NewsRestController")
class NewsRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private NewsService newsService;
    @MockBean
    private NewsMapper newsMapper;
    @MockBean
    private CommentMapper commentMapper;

    private News news;
    private NewsDTO newsDTO;

    @Nested
    @ExtendWith(ValidNewsDTOParameterResolver.class)
    @DisplayName("Using valid newsDTO")
    public class ValidData {
        @Test
        @DisplayName("After saving valid news returns 201-status and news id")
        void givenNews_whenCreateValidNews_thenReturnStatusCreatedAndNewsId(NewsDTO newsDTO) throws Exception {
            Mockito.when(newsMapper.fromDTO(newsDTO, News.builder().build())).thenReturn(news);
            Mockito.when(newsService.save(news)).thenReturn(news);

            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(newsDTO)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().string(newsDTO.getId()));
        }

        @Test
        @DisplayName("After updating news by id returns 200-status and newsDTO")
        void givenNews_whenUpdateNews_thenReturnStatusOkAndNewsDTO() throws Exception {
            Mockito.when(newsService.getById(1L)).thenReturn(Optional.of(news));
            Mockito.when(newsMapper.fromDTO(newsDTO, news)).thenReturn(news);
            Mockito.when(newsService.save(news)).thenReturn(news);
            Mockito.when(newsMapper.toDTO(news, newsDTO)).thenReturn(newsDTO);

            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", news.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(newsDTO)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(newsDTO.getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(newsDTO.getTitle())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is(newsDTO.getText())));
        }
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
            Mockito.when(newsService.getById(1L)).thenThrow(NewsNotFoundException.class);

            mockMvc.perform(MockMvcRequestBuilders.get("/news/{id}", news.getId()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        @DisplayName("After updating non-existing news by id returns 404-status")
        void givenId_whenUpdateNonExistingNews_thenReturnStatusBadRequest() throws Exception {
            Mockito.when(newsService.getById(1L)).thenThrow(NewsNotFoundException.class);

            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", news.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(newsDTO)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        @DisplayName("After deleting non-existing news by id returns 404-status")
        void givenId_whenDeleteNonExistingNews_thenReturnStatusBadRequest() throws Exception {
            Mockito.when(newsService.getById(1L)).thenThrow(NewsNotFoundException.class);

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
            Mockito.when(newsService.getById(1L)).thenReturn(Optional.of(news));
            Mockito.when(newsMapper.toDTO(news, NewsDTO.builder().build())).thenReturn(newsDTO);

            mockMvc.perform(MockMvcRequestBuilders.get("/news/{id}", news.getId()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(newsDTO.getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(newsDTO.getTitle())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is(newsDTO.getText())));
        }

        @Test
        @DisplayName("After deleting news by id returns 200-status")
        void givenId_whenDeleteNews_thenReturnStatusOk() throws Exception {
            Mockito.when(newsService.getById(1L)).thenReturn(Optional.of(news));

            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", news.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(newsDTO)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @BeforeEach
    void setUp() {
        news = News.builder()
                .id(1L)
                .title("title")
                .text("text")
                .build();

        newsDTO = NewsDTO.builder()
                .id("1")
                .title("title")
                .text("text")
                .build();
    }

    @AfterEach
    void tearDown() {
        news = null;
        newsDTO = null;
    }
}