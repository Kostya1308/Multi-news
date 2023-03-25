package by.clevertec.service.impl;

import by.clevertec.entity.News;
import by.clevertec.repository.NewsRepository;
import by.clevertec.service.interfaces.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class NewsServiceImpl implements NewsService {

    NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    public Optional<News> getById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public Optional<News> getByIdWithComments(Long id) {
        return newsRepository.findByIdWithComments(id);
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public Page<News> getAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    @Override
    public Page<News> getAllByTitleContainsPageable(String key, Pageable pageable) {
        return newsRepository.findAllByTitleContainsPageable(key, pageable);
    }

    @Override
    public Page<News> getAllByDateTimeCreateLessThanPageable(LocalDateTime dateTime, Pageable pageable) {
        return newsRepository.findAllByDateTimeCreateLessThanPageable(dateTime, pageable);
    }

    @Override
    public Page<News> getAllByDateTimeCreateGreaterThanPageable(LocalDateTime dateTime, Pageable pageable) {
        return newsRepository.findAllByDateTimeCreateGreaterThanPageable(dateTime, pageable);
    }
}
