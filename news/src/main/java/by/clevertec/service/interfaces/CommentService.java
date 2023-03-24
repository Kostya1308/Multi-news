package by.clevertec.service.interfaces;

import by.clevertec.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentService {

    Comment save(Comment comment);

    void deleteById(Long id);

    Optional<Comment> getByIdWithNews(@Param("id") Long id);

    Page<Comment> getAllByNewsPageable(@Param("newsId") Long newsId, Pageable pageable);

    Page<Comment> getAllByUsernameWithNewsPageable(@Param("username") String username, Pageable pageable);
}
