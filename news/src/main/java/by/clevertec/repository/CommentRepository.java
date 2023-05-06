package by.clevertec.repository;

import by.clevertec.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.id =:id")
    @EntityGraph("Comment.news")
    Optional<Comment> findByIdWithNews(@Param("id") Long id);

    @Query("SELECT c FROM Comment c WHERE c.news.id =:newsId")
    @EntityGraph("Comment.news")
    Page<Comment> findAllByNewsPageable(@Param("newsId") Long newsId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.username =:username")
    @EntityGraph("Comment.news")
    Page<Comment> findAllByUsernameWithNewsPageable(@Param("username") String username, Pageable pageable);
}
