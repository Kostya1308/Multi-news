package by.clevertec.controller;

import by.clevertec.dto.NewsDTO;
import by.clevertec.entity.News;
import by.clevertec.exception.NewsNotFoundException;
import by.clevertec.mapper.NewsMapper;
import by.clevertec.service.interfaces.NewsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/news")
public class NewsRestController {
    @Autowired
    NewsService newsService;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    ObjectMapper objectMapper;

    @PostMapping(value = "/add")
    public ResponseEntity<String> createNews(@RequestBody @Validated NewsDTO newsDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.toString(), HttpStatus.NOT_ACCEPTABLE);
        }

        News news = newsMapper.fromDTO(newsDTO);
        News persistNews = newsService.save(news);

        return new ResponseEntity<>(String.valueOf(persistNews.getId()), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<String> getNews(@PathVariable("id") Long id) {
        AtomicReference<ResponseEntity<String>> responseEntity = new AtomicReference<>();
        Optional<News> news = newsService.getById(id);

        news.ifPresentOrElse(itemNews -> {
            try {
                NewsDTO newsDTO = newsMapper.toDTO(itemNews);
                String newsJson = objectMapper.writeValueAsString(newsDTO);
                responseEntity.set(new ResponseEntity<>(newsJson, HttpStatus.OK));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            throw new NewsNotFoundException("News doesn't exist");
        });

        return responseEntity.get();
    }

    @PostMapping(value = "/update/{id}")
    public ResponseEntity<String> updateNews(@PathVariable("id") Long id, @RequestBody NewsDTO newNewsDTO) {
        AtomicReference<ResponseEntity<String>> responseEntity = new AtomicReference<>();

        Optional<News> news = newsService.getById(id);
        news.ifPresentOrElse(itemNews -> {
            try {
                News updatedNews = newsMapper.updateFromDTO(itemNews, newNewsDTO);
                newsService.save(updatedNews);
                String newsJson = objectMapper.writeValueAsString(updatedNews);
                responseEntity.set(new ResponseEntity<>(newsJson, HttpStatus.OK));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            throw new NewsNotFoundException("News doesn't exist");
        });

        return responseEntity.get();
    }

    @GetMapping(value = "delete/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable("id") Long id) {
        newsService.deleteById(id);
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<String>> getAllNews
            (@RequestParam(name = "page", defaultValue = "0") String page,
             @RequestParam(name = "size", defaultValue = "3") String size,
             @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy,
             @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<News> news = newsService.getAll(pageRequest);
        List<String> newsJsonList = getNewsJsonList(news);

        return new ResponseEntity<>(newsJsonList, HttpStatus.OK);
    }


    @GetMapping(value = "/search-by-title")
    @Transactional
    public ResponseEntity<List<String>> getAllNewsByTitle
            (@RequestParam(name = "title") String title,
             @RequestParam(name = "page", defaultValue = "0") String page,
             @RequestParam(name = "size", defaultValue = "3") String size,
             @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy,
             @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<News> news = newsService.getAllByTitleContainsPageable(title, pageRequest);
        List<String> newsJsonList = getNewsJsonList(news);

        return new ResponseEntity<>(newsJsonList, HttpStatus.OK);
    }

    @GetMapping(value = "/search-by-date-time")
    @Transactional
    public ResponseEntity<List<String>> getAllNewsByDateTimeCreateLessThan
            (@RequestParam(name = "dateTime") String dateTime,
             @RequestParam("less") boolean less,
             @RequestParam(name = "page", defaultValue = "0") String page,
             @RequestParam(name = "size", defaultValue = "3") String size,
             @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy,
             @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<News> news;
        if (less) {
            news = newsService.getAllByDateTimeCreateLessThanPageable(LocalDateTime.parse(dateTime), pageRequest);
        } else {
            news = newsService.getAllByDateTimeCreateGreaterThanPageable(LocalDateTime.parse(dateTime), pageRequest);
        }

        List<String> newsJsonList = getNewsJsonList(news);

        return new ResponseEntity<>(newsJsonList, HttpStatus.OK);
    }

    private List<String> getNewsJsonList(Page<News> news) {
        return news.stream().map((item -> {
            try {
                NewsDTO newsDTO = newsMapper.toDTO(item);
                return objectMapper.writeValueAsString(newsDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        })).toList();
    }
}
