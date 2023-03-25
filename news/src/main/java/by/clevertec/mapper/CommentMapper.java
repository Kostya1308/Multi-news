package by.clevertec.mapper;

import by.clevertec.dto.CommentDto;
import by.clevertec.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommentMapper implements Mapper<Comment, CommentDto> {

    @Override
    public CommentDto toDTO(Comment comment, CommentDto commentDTO) {
        commentDTO.setId(String.valueOf(comment.getId()));
        commentDTO.setText(comment.getText());
        commentDTO.setUsername(comment.getUsername());
        Optional.ofNullable(comment.getNews())
                .ifPresent(itemNews -> commentDTO.setNewsId(String.valueOf(itemNews.getId())));

        return commentDTO;
    }

    @Override
    public Comment fromDTO(CommentDto commentDTO, Comment comment) {
        Optional.ofNullable(commentDTO.getId())
                .ifPresent(item -> comment.setId(Long.parseLong(item)));
        Optional.ofNullable(commentDTO.getUsername())
                .ifPresent(comment::setUsername);
        Optional.ofNullable(commentDTO.getText())
                .ifPresent(comment::setText);

        return comment;
    }
}
