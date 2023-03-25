package by.clevertec.service.interfaces;

import by.clevertec.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NewsService{

    News save(News news);

    Optional<News> getById(Long id);

    Optional<News> getByIdWithComments(Long id);

    void deleteById(Long id);

    Page<News> getAll(Pageable pageable);

    Page<News> getAllByTitleContainsPageable(String key, Pageable pageable);

    Page<News> getAllByDateTimeCreateLessThanPageable(LocalDateTime dateTime, Pageable pageable);

    Page<News> getAllByDateTimeCreateGreaterThanPageable(LocalDateTime dateTime, Pageable pageable);
}
