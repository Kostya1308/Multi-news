package by.clevertec.mapper;

import by.clevertec.dto.NewsDTO;
import by.clevertec.entity.News;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NewsMapper implements Mapper<News, NewsDTO> {

    @Override
    public NewsDTO toDTO(News news, NewsDTO newsDTO) {
        newsDTO.setId(String.valueOf(news.getId()));
        newsDTO.setTitle(news.getTitle());
        newsDTO.setText(news.getText());

        return newsDTO;
    }

    @Override
    public News fromDTO(NewsDTO newsDTO, News news) {
        Optional.ofNullable(newsDTO.getId())
                .ifPresent(item -> news.setId(Long.parseLong(item)));
        Optional.ofNullable(newsDTO.getTitle())
                .ifPresent(news::setTitle);
        Optional.ofNullable(newsDTO.getText())
                .ifPresent(news::setText);

        return news;
    }
}
