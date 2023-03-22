package by.clevertec.mapper;

import by.clevertec.dto.CommentDTO;
import by.clevertec.entity.Comment;
import by.clevertec.factory.reaction.CommentFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommentMapper implements Mapper<Comment, CommentDTO> {

    CommentFactory commentFactory;

    public CommentMapper(CommentFactory commentFactory) {
        this.commentFactory = commentFactory;
    }

    @Override
    public CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(String.valueOf(comment.getId()));
        commentDTO.setText(comment.getText());
        commentDTO.setUsername(comment.getUsername());
        commentDTO.setNewsId(String.valueOf(comment.getNews().getId()));

        return commentDTO;
    }

    @Override
    public Comment fromDTO(CommentDTO commentDTO) {
        Comment comment = (Comment) commentFactory.create();
        copyFieldsFromNewsDTO(comment, commentDTO);

        return comment;
    }

    public Comment updateFromDTO(Comment comment, CommentDTO commentDTO) {
        copyFieldsFromNewsDTO(comment, commentDTO);

        return comment;
    }

    private void copyFieldsFromNewsDTO(Comment comment, CommentDTO commentDTO) {
        Optional.ofNullable(commentDTO.getId())
                .ifPresent(item -> comment.setId(Long.parseLong(item)));
        Optional.ofNullable(commentDTO.getUsername())
                .ifPresent(comment::setUsername);
        Optional.ofNullable(commentDTO.getText())
                .ifPresent(comment::setText);
    }
}
