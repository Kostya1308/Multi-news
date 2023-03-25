package by.clevertec.repository;

import by.clevertec.entity.Comment;
import by.clevertec.entity.News;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CommentRepositoryIntegrationTest {
    @Autowired
    CommentRepository commentRepository;

    @Test
    void findByIdWithNewsReturnCommentWithNewsTest() {
        Optional<Comment> comment = commentRepository.findById(1L);

        comment.ifPresent(item -> {
            News news = item.getNews();
            assertNotNull(news);
        });
    }

    @Test
    void findAllByNewsPageable() {
    }
}