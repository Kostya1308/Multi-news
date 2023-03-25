package by.clevertec.mapper;

import by.clevertec.dto.CommentDto;
import by.clevertec.entity.Comment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommentMapperTest {

    private final CommentMapper commentMapper = new CommentMapper();

    @Test
    void toDTO() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Java is awesome");
        comment.setUsername("Kostya");

        CommentDto commentDto = commentMapper.toDTO(comment, new CommentDto());

        Assertions.assertEquals(String.valueOf(comment.getId()), commentDto.getId());
    }

    @Test
    void fromDTO() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Java is awesome");
        commentDto.setUsername("Kostya");

        Comment comment = commentMapper.fromDTO(commentDto, new Comment());

        Assertions.assertEquals(commentDto.getUsername(), comment.getUsername());
    }
}