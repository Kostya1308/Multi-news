package by.clevertec.mapper;

import by.clevertec.dto.NewsDto;
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

        NewsDto newsDTO = newsMapper.toDTO(news, new NewsDto());
        Assertions.assertEquals(String.valueOf(news.getId()), newsDTO.getId());
    }

    @Test
    void fromDTO() {
        NewsDto newsDTO = new NewsDto();
        newsDTO.setId("1");
        newsDTO.setTitle("Java");

        News news = newsMapper.fromDTO(newsDTO, new News());

        Assertions.assertEquals(newsDTO.getId(), String.valueOf(news.getId()));
    }
}