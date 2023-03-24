package by.clevertec.service.impl;

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

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Optional<Comment> getByIdWithNews(Long id) {
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
