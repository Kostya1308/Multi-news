package by.clevertec.service.impl;

import by.clevertec.cache.LRUCache;
import by.clevertec.entity.Comment;
import by.clevertec.repository.CommentRepository;
import by.clevertec.service.interfaces.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    LRUCache<Long, Comment> lruCache;

    public CommentServiceImpl(CommentRepository commentRepository, LRUCache<Long, Comment> lruCache) {
        this.commentRepository = commentRepository;
        this.lruCache = lruCache;
    }

    @Override
    public Comment save(Comment comment) {
        Comment persistComment = commentRepository.save(comment);
        lruCache.put(persistComment.getId(), persistComment);

        return persistComment;
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
        lruCache.remove(id);
    }

    @Override
    public Optional<Comment> getByIdWithNews(Long id) {
        Optional<Comment> comment = lruCache.get(id);

        if (comment.isEmpty()) {
            comment = commentRepository.findByIdWithNews(id);
            comment.ifPresent(itemComment -> lruCache.put(itemComment.getId(), itemComment));
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
