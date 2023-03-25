package by.clevertec.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class CommentDto {

    private String id;
    @NotNull(message = "Empty text field")
    private String text;
    private String username;
    private String newsId;
}
