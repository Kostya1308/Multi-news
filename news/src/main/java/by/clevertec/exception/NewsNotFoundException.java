package by.clevertec.exception;

public class NewsNotFoundException extends RuntimeException{
    public NewsNotFoundException(String message) {
        super(message);
    }
}
