package by.clevertec.service.impl;

import by.clevertec.cache.Cache;
import by.clevertec.entity.News;
import by.clevertec.repository.NewsRepository;
import by.clevertec.service.interfaces.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {

    @Value("${cache.type.name}")
    private String cacheName;
    private Cache<Long, News> cache;
    private final NewsRepository newsRepository;


    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Autowired
    public void setCache(ApplicationContext context) {
        this.cache = (Cache<Long, News>) context.getBean(cacheName);
    }

    @Override
    public News save(News news) {
        News persistNews = newsRepository.save(news);
        cache.put(persistNews.getId(), persistNews);

        return persistNews;
    }

    @Override
    public Optional<News> getById(Long id) {
        Optional<News> news = cache.get(id);

        if (news.isEmpty()) {
            news = newsRepository.findById(id);
            news.ifPresent(itemNews -> cache.put(itemNews.getId(), itemNews));
        }

        return news;
    }

    @Override
    public Optional<News> getByIdWithComments(Long id) {
        Optional<News> news = cache.get(id);

        if (news.isEmpty() || news.get().getComments() == null) {
            news = newsRepository.findByIdWithComments(id);
            news.ifPresent(itemNews -> cache.put(itemNews.getId(), itemNews));
        }

        return news;
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
        cache.remove(id);
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
