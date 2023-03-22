package by.clevertec.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Getter
@Setter
@Validated
public class NewsDTO {

    private String id;
    @NotNull(message = "Empty title field")
    private String title;
    @NotNull(message = "Empty text field")
    private String text;
}
