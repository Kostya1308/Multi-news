package by.clevertec.controller;

import by.clevertec.aspect.LogRequestResponse;
import by.clevertec.dto.CommentDTO;
import by.clevertec.dto.NewsDTO;
import by.clevertec.entity.Comment;
import by.clevertec.entity.News;
import by.clevertec.exception.NewsNotFoundException;
import by.clevertec.mapper.CommentMapper;
import by.clevertec.mapper.NewsMapper;
import by.clevertec.service.interfaces.NewsService;
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
import java.util.Set;

@RestController
@RequestMapping("/news")
public class NewsRestController {
    @Autowired
    NewsService newsService;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    CommentMapper commentMapper;

    @RequestMapping(method = RequestMethod.POST)
    @LogRequestResponse
    public ResponseEntity<String> createNews(@RequestBody @Validated NewsDTO newsDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.toString(), HttpStatus.NOT_ACCEPTABLE);
        }

        News news = newsMapper.fromDTO(newsDTO, new News());
        News persistNews = newsService.save(news);

        return new ResponseEntity<>(String.valueOf(persistNews.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @LogRequestResponse
    public ResponseEntity<NewsDTO> getNews(@PathVariable("id") Long id) {

        News news = newsService.getById(id)
                .orElseThrow(() -> new NewsNotFoundException("News doesn't exist"));
        NewsDTO newsDTO = newsMapper.toDTO(news, new NewsDTO());

        return ResponseEntity.ok(newsDTO);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/comments")
    @LogRequestResponse
    public ResponseEntity<NewsDTO> getNewsWithComments(@PathVariable("id") Long id) {

        News news = newsService.getById(id)
                .orElseThrow(() -> new NewsNotFoundException("News doesn't exist"));

        NewsDTO newsDTO = newsMapper.toDTO(news, new NewsDTO());
        Set<Comment> comments = news.getComments();
        List<CommentDTO> commentDTOList = getCommentDtoListFromCommentSet(comments);
        newsDTO.setCommentDTOList(commentDTOList);
        return ResponseEntity.ok(newsDTO);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    @LogRequestResponse
    public ResponseEntity<NewsDTO> updateNews(@PathVariable("id") Long id, @RequestBody NewsDTO newNewsDTO) {

        News news = newsService.getById(id).orElseThrow(() -> new NewsNotFoundException("News doesn't exist"));
        News updatedNews = newsMapper.fromDTO(newNewsDTO, news);
        newsService.save(updatedNews);
        NewsDTO newsDTOUpdated = newsMapper.toDTO(updatedNews, newNewsDTO);

        return ResponseEntity.ok(newsDTOUpdated);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    @LogRequestResponse
    public ResponseEntity<String> deleteNews(@PathVariable("id") Long id) {
        newsService.deleteById(id);
        return ResponseEntity.ok(String.valueOf(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    @LogRequestResponse
    public ResponseEntity<List<NewsDTO>> getAllNews
            (@RequestParam(name = "page", defaultValue = "0") String page,
             @RequestParam(name = "size", defaultValue = "3") String size,
             @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy,
             @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<News> news = newsService.getAll(pageRequest);
        List<NewsDTO> newsDTOList = getNewsDtoList(news);

        return ResponseEntity.ok(newsDTOList);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/title")
    @Transactional
    @LogRequestResponse
    public ResponseEntity<List<NewsDTO>> getAllNewsByTitle
            (@RequestParam(name = "title") String title,
             @RequestParam(name = "page", defaultValue = "0") String page,
             @RequestParam(name = "size", defaultValue = "3") String size,
             @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy,
             @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<News> news = newsService.getAllByTitleContainsPageable(title, pageRequest);
        List<NewsDTO> newsDTOList = getNewsDtoList(news);

        return ResponseEntity.ok(newsDTOList);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/date-time")
    @Transactional
    @LogRequestResponse
    public ResponseEntity<List<NewsDTO>> getAllNewsByDateTimeCreateLessThan
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

        List<NewsDTO> newsDTOList = getNewsDtoList(news);
        return ResponseEntity.ok(newsDTOList);
    }

    private List<NewsDTO> getNewsDtoList(Page<News> news) {
        return news.stream()
                .map((item -> newsMapper.toDTO(item, new NewsDTO())))
                .toList();
    }

    private List<CommentDTO> getCommentDtoListFromCommentSet(Set<Comment> comments) {
        return comments.stream()
                .map((item -> commentMapper.toDTO(item, new CommentDTO())))
                .toList();
    }
}
