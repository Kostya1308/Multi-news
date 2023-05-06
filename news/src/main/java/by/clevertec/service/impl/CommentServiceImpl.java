package by.clevertec.service.impl;

import by.clevertec.cache.Cache;
import by.clevertec.entity.Comment;
import by.clevertec.repository.CommentRepository;
import by.clevertec.service.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final Cache<Long, Comment> cache;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, @Qualifier("lru") Cache<Long, Comment> cache) {
        this.commentRepository = commentRepository;
        this.cache = cache;
    }

    @Override
    public Comment save(Comment comment) {
        Comment persistComment = commentRepository.save(comment);
        cache.put(persistComment.getId(), persistComment);

        return persistComment;
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
        cache.remove(id);
    }

    @Override
    public Optional<Comment> getByIdWithNews(Long id) {
        Optional<Comment> comment = cache.get(id);

        if (comment.isEmpty()) {
            comment = commentRepository.findByIdWithNews(id);
            comment.ifPresent(itemComment -> cache.put(itemComment.getId(), itemComment));
        }

        return commentRepository.findByIdWithNews(id);
    }

    @Override
    public Page<Comment> getAllByNewsPageable(Long newsId, Pageable pageable) {
        return commentRepository.findAllByNewsPageable(newsId, pageable);
    }

    @Override
    public Page<Comment> getAllByUsernameWithNewsPageable(String username, Pageable pageable) {
        return commentRepository.findAllByUsernameWithNewsPageable(username, pageable);
    }
}
