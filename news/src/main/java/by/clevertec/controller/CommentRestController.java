package by.clevertec.controller;

import by.clevertec.dto.CommentDTO;
import by.clevertec.entity.Comment;
import by.clevertec.entity.News;
import by.clevertec.exception.CommentNotFoundException;
import by.clevertec.mapper.CommentMapper;
import by.clevertec.service.interfaces.CommentService;
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

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/comment")
public class CommentRestController {
    @Autowired
    CommentService commentService;
    @Autowired
    NewsService newsService;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ObjectMapper objectMapper;

    @PostMapping(value = "/add")
    public ResponseEntity<String> createComment(@RequestParam("newsId") Long newsId, @RequestBody @Validated CommentDTO commentDTO, BindingResult bindingResult) {
        AtomicReference<ResponseEntity<String>> responseEntity = new AtomicReference<>();

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.toString(), HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<News> news = newsService.getById(newsId);
        news.ifPresentOrElse(newsItem -> {
            Comment comment = commentMapper.fromDTO(commentDTO);
            comment.setNews(newsItem);
            commentService.save(comment);
            responseEntity.set(new ResponseEntity<>(String.valueOf(comment.getId()), HttpStatus.CREATED));
        }, () -> {
            throw new CommentNotFoundException("Comment doesn't exist");
        });

        return responseEntity.get();
    }

    @GetMapping(value = "/{commentId}")
    @Transactional
    public ResponseEntity<String> getComment(@PathVariable("commentId") Long commentId) {
        AtomicReference<ResponseEntity<String>> responseEntity = new AtomicReference<>();
        Optional<Comment> comment = commentService.getByIdWithNews(commentId);

        comment.ifPresentOrElse(itemComment -> {
            try {
                CommentDTO commentDTO = commentMapper.toDTO(itemComment);
                String commentJson = objectMapper.writeValueAsString(commentDTO);
                responseEntity.set(new ResponseEntity<>(commentJson, HttpStatus.OK));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            throw new CommentNotFoundException("Comment doesn't exist");
        });

        return responseEntity.get();
    }

    @PostMapping(value = "/update/{id}")
    @Transactional
    public ResponseEntity<String> updateComment(@PathVariable("id") Long id, @RequestBody CommentDTO newCommentDTO) {
        AtomicReference<ResponseEntity<String>> responseEntity = new AtomicReference<>();

        Optional<Comment> comment = commentService.getByIdWithNews(id);
        comment.ifPresentOrElse(itemComment -> {
            try {
                Comment updatedComment = commentMapper.updateFromDTO(itemComment, newCommentDTO);
                updatedComment.setNews(itemComment.getNews());
                commentService.save(updatedComment);
                String commentJson = objectMapper.writeValueAsString(updatedComment);
                responseEntity.set(new ResponseEntity<>(commentJson, HttpStatus.OK));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            throw new CommentNotFoundException("Comment doesn't exist");
        });

        return responseEntity.get();
    }

    @GetMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteById(id);
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @GetMapping(value = "/news/{newsId}")
    @Transactional
    public ResponseEntity<List<String>> getCommentsByNewsId(@PathVariable("newsId") String newsId, @RequestParam(name = "page", defaultValue = "0") String page, @RequestParam(name = "size", defaultValue = "3") String size, @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy, @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<Comment> comments = commentService.getAllByNewsPageable(Long.valueOf(newsId), pageRequest);
        List<String> commentJsonList = getCommentJsonList(comments);

        return new ResponseEntity<>(commentJsonList, HttpStatus.OK);
    }

    @GetMapping(value = "/username/{username}")
    @Transactional
    public ResponseEntity<List<String>> getCommentsByUsername(@PathVariable("username") String username, @RequestParam(name = "page", defaultValue = "0") String page, @RequestParam(name = "size", defaultValue = "3") String size, @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy, @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<Comment> comments = commentService.getAllByUsernameWithNewsPageable(username, pageRequest);
        List<String> commentJsonList = getCommentJsonList(comments);

        return new ResponseEntity<>(commentJsonList, HttpStatus.OK);
    }

    private List<String> getCommentJsonList(Page<Comment> comments) {
        return comments.stream().map((item -> {
            try {
                CommentDTO commentDTO = commentMapper.toDTO(item);
                return objectMapper.writeValueAsString(commentDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        })).toList();
    }


}
