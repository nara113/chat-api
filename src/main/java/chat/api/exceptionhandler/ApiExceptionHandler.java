package chat.api.exceptionhandler;

import chat.api.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Response<String> handle(MethodArgumentNotValidException ex) {
        return Response.of(HttpStatus.BAD_REQUEST.value(), ex.getFieldErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    private Response<String> illegalEx(Exception ex) {
        return Response.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
