package by.clevertec.mapper;

import by.clevertec.dto.NewsDTO;
import by.clevertec.entity.News;
import by.clevertec.factory.content.NewsFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NewsMapper implements Mapper<News, NewsDTO> {

    private final NewsFactory newsFactory;

    public NewsMapper(NewsFactory newsFactory) {
        this.newsFactory = newsFactory;
    }

    @Override
    public NewsDTO toDTO(News news) {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(String.valueOf(news.getId()));
        newsDTO.setTitle(news.getTitle());
        newsDTO.setText(news.getText());

        return newsDTO;
    }

    @Override
    public News fromDTO(NewsDTO newsDTO) {
        News news = (News) newsFactory.create();
        copyFieldsFromNewsDTO(news, newsDTO);

        return news;
    }

    public News updateFromDTO(News news, NewsDTO newsDTO) {
        copyFieldsFromNewsDTO(news, newsDTO);

        return news;
    }

    private void copyFieldsFromNewsDTO(News news, NewsDTO newsDTO) {
        Optional.ofNullable(newsDTO.getId())
                .ifPresent(item -> news.setId(Long.parseLong(item)));
        Optional.ofNullable(newsDTO.getTitle())
                .ifPresent(news::setTitle);
        Optional.ofNullable(newsDTO.getText())
                .ifPresent(news::setText);
    }
}
