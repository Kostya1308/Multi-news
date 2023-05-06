package by.clevertec.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler({NewsNotFoundException.class, CommentNotFoundException.class})
    public ResponseEntity<String> handleNotFoundConflict(Exception exception) {
        return ResponseEntity.status(404).body(exception.getMessage());
    }
}
