package by.clevertec.mapper;

import by.clevertec.dto.NewsDTO;
import by.clevertec.entity.News;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NewsMapperTest {
    private final NewsMapper newsMapper = new NewsMapper();

    @Test
    void toDTOTest() {
        News news = new News();
        news.setId(1L);
        news.setTitle("Java");

        NewsDTO newsDTO = newsMapper.toDTO(news, new NewsDTO());
        Assertions.assertEquals(String.valueOf(news.getId()), newsDTO.getId());
    }

    @Test
    void fromDTO() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId("1");
        newsDTO.setTitle("Java");

        News news = newsMapper.fromDTO(newsDTO, new News());

        Assertions.assertEquals(newsDTO.getId(), String.valueOf(news.getId()));
    }
}