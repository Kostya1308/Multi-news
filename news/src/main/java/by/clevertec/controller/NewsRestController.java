package by.clevertec.controller;

import by.clevertec.dto.CommentDto;
import by.clevertec.dto.NewsDto;
import by.clevertec.entity.Comment;
import by.clevertec.entity.News;
import by.clevertec.exception.NewsNotFoundException;
import by.clevertec.mapper.CommentMapper;
import by.clevertec.mapper.NewsMapper;
import by.clevertec.service.interfaces.NewsService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RestController
@RequestMapping("/news")
public class NewsRestController {
    @Autowired
    NewsService newsService;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    CommentMapper commentMapper;

    @RequestMapping(method = RequestMethod.GET, path = "/sss")
    public ResponseEntity<String> home() {
        log.info("info");
        log.warn("warn");
        log.debug("debug");
        log.error("error");
        return new ResponseEntity<>("asdasdasd", HttpStatus.OK);

    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> createNews(@RequestBody @Validated NewsDto newsDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.toString(), HttpStatus.NOT_ACCEPTABLE);
        }

        News news = newsMapper.fromDTO(newsDTO, new News());
        News persistNews = newsService.save(news);

        return new ResponseEntity<>(String.valueOf(persistNews.getId()), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<NewsDto> getNews(@PathVariable("id") Long id) {
        AtomicReference<ResponseEntity<NewsDto>> responseEntity = new AtomicReference<>();
        Optional<News> news = newsService.getById(id);

        news.ifPresentOrElse(itemNews -> {
            NewsDto newsDTO = newsMapper.toDTO(itemNews, new NewsDto());
            responseEntity.set(new ResponseEntity<>(newsDTO, HttpStatus.OK));
        }, () -> {
            throw new NewsNotFoundException("News doesn't exist");
        });

        return responseEntity.get();
    }

    @GetMapping(value = "/{id}/comments")
    public ResponseEntity<NewsDto> getNewsWithComments(@PathVariable("id") Long id) {
        AtomicReference<ResponseEntity<NewsDto>> responseEntity = new AtomicReference<>();
        Optional<News> news = newsService.getByIdWithComments(id);

        news.ifPresentOrElse(itemNews -> {
            NewsDto newsDTO = newsMapper.toDTO(itemNews, new NewsDto());
            Set<Comment> comments = itemNews.getComments();
            List<CommentDto> commentDtoList = getCommentDtoListFromCommentSet(comments);
            newsDTO.setCommentDtoList(commentDtoList);
            responseEntity.set(new ResponseEntity<>(newsDTO, HttpStatus.OK));
        }, () -> {
            throw new NewsNotFoundException("News doesn't exist");
        });

        return responseEntity.get();
    }

    @PostMapping(value = "/update/{id}")
    public ResponseEntity<NewsDto> updateNews(@PathVariable("id") Long id, @RequestBody NewsDto newNewsDTO) {
        AtomicReference<ResponseEntity<NewsDto>> responseEntity = new AtomicReference<>();

        Optional<News> news = newsService.getById(id);
        news.ifPresentOrElse(itemNews -> {
            News updatedNews = newsMapper.fromDTO(newNewsDTO, itemNews);
            newsService.save(updatedNews);
            NewsDto newsDtoUpdated = newsMapper.toDTO(updatedNews, newNewsDTO);
            responseEntity.set(new ResponseEntity<>(newsDtoUpdated, HttpStatus.OK));
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
    public ResponseEntity<List<NewsDto>> getAllNews
            (@RequestParam(name = "page", defaultValue = "0") String page,
             @RequestParam(name = "size", defaultValue = "3") String size,
             @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy,
             @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<News> news = newsService.getAll(pageRequest);
        List<NewsDto> newsDtoList = getNewsDtoList(news);

        return new ResponseEntity<>(newsDtoList, HttpStatus.OK);
    }


    @GetMapping(value = "/search-by-title")
    @Transactional
    public ResponseEntity<List<NewsDto>> getAllNewsByTitle
            (@RequestParam(name = "title") String title,
             @RequestParam(name = "page", defaultValue = "0") String page,
             @RequestParam(name = "size", defaultValue = "3") String size,
             @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy,
             @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<News> news = newsService.getAllByTitleContainsPageable(title, pageRequest);
        List<NewsDto> newsDtoList = getNewsDtoList(news);

        return new ResponseEntity<>(newsDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/search-by-date-time")
    @Transactional
    public ResponseEntity<List<NewsDto>> getAllNewsByDateTimeCreateLessThan
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

        List<NewsDto> newsDtoList = getNewsDtoList(news);

        return new ResponseEntity<>(newsDtoList, HttpStatus.OK);
    }

    private List<NewsDto> getNewsDtoList(Page<News> news) {
        return news.stream()
                .map((item -> newsMapper.toDTO(item, new NewsDto())))
                .toList();
    }

    private List<CommentDto> getCommentDtoListFromCommentSet(Set<Comment> comments) {
        return comments.stream()
                .map((item -> commentMapper.toDTO(item, new CommentDto())))
                .toList();
    }
}
