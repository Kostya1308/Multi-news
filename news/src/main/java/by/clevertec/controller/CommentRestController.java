package by.clevertec.controller;

import by.clevertec.dto.CommentDTO;
import by.clevertec.entity.Comment;
import by.clevertec.entity.News;
import by.clevertec.exception.CommentNotFoundException;
import by.clevertec.mapper.CommentMapper;
import by.clevertec.service.interfaces.CommentService;
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

    @RequestMapping(method = RequestMethod.POST, path = "/add")
    public ResponseEntity<String> createComment(@RequestParam("newsId") Long newsId,
                                                @RequestBody @Validated CommentDTO commentDTO,
                                                BindingResult bindingResult) {
        AtomicReference<ResponseEntity<String>> responseEntity = new AtomicReference<>();

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.toString(), HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<News> news = newsService.getById(newsId);
        news.ifPresentOrElse(newsItem -> {
            Comment comment = commentMapper.fromDTO(commentDTO, new Comment());
            comment.setNews(newsItem);
            commentService.save(comment);
            responseEntity.set(new ResponseEntity<>(String.valueOf(comment.getId()), HttpStatus.CREATED));
        }, () -> {
            throw new CommentNotFoundException("Comment doesn't exist");
        });

        return responseEntity.get();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{commentId}")
    @Transactional
    public ResponseEntity<CommentDTO> getComment(@PathVariable("commentId") Long commentId) {
        AtomicReference<ResponseEntity<CommentDTO>> responseEntity = new AtomicReference<>();
        Optional<Comment> comment = commentService.getByIdWithNews(commentId);

        comment.ifPresentOrElse(itemComment -> {
            CommentDTO commentDTO = commentMapper.toDTO(itemComment, new CommentDTO());
            responseEntity.set(new ResponseEntity<>(commentDTO, HttpStatus.OK));
        }, () -> {
            throw new CommentNotFoundException("Comment doesn't exist");
        });

        return responseEntity.get();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/update/{id}")
    @Transactional
    public ResponseEntity<CommentDTO> updateComment(@PathVariable("id") Long id, @RequestBody CommentDTO newCommentDTO) {
        AtomicReference<ResponseEntity<CommentDTO>> responseEntity = new AtomicReference<>();

        Optional<Comment> comment = commentService.getByIdWithNews(id);
        comment.ifPresentOrElse(itemComment -> {
            Comment updatedComment = commentMapper.fromDTO(newCommentDTO, itemComment);
            updatedComment.setNews(itemComment.getNews());
            commentService.save(updatedComment);
            CommentDTO commentDTOUpdated = commentMapper.toDTO(updatedComment, new CommentDTO());
            responseEntity.set(new ResponseEntity<>(commentDTOUpdated, HttpStatus.OK));
        }, () -> {
            throw new CommentNotFoundException("Comment doesn't exist");
        });

        return responseEntity.get();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteById(id);
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/news/{newsId}")
    @Transactional
    public ResponseEntity<List<CommentDTO>> getCommentsByNewsId(@PathVariable("newsId") String newsId,
                                                                @RequestParam(name = "page", defaultValue = "0") String page,
                                                                @RequestParam(name = "size", defaultValue = "3") String size,
                                                                @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy,
                                                                @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<Comment> comments = commentService.getAllByNewsPageable(Long.valueOf(newsId), pageRequest);
        List<CommentDTO> commentDTOList = getCommentDtoListFromPage(comments);

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/username/{username}")
    @Transactional
    public ResponseEntity<List<CommentDTO>> getCommentsByUsername(@PathVariable("username") String username,
                                                                  @RequestParam(name = "page", defaultValue = "0") String page,
                                                                  @RequestParam(name = "size", defaultValue = "3") String size,
                                                                  @RequestParam(name = "sort-by", defaultValue = "dateTimeCreate") String sortBy,
                                                                  @RequestParam(name = "sort-dir", defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = ControllerUtil.getPageRequest(page, size, sortBy, sortDir);
        Page<Comment> comments = commentService.getAllByUsernameWithNewsPageable(username, pageRequest);
        List<CommentDTO> commentDTOList = getCommentDtoListFromPage(comments);

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    private List<CommentDTO> getCommentDtoListFromPage(Page<Comment> comments) {
        return comments.stream()
                .map((item -> commentMapper.toDTO(item, new CommentDTO())))
                .toList();
    }
}
