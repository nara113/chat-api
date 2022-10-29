package chat.api.common.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class Response<T> {
    private int status;
    private T data;
    private LocalDateTime timestamp;

    private Response(int status, T data) {
        this.status = status;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> Response<T> of(int status, T data) {
        return new Response<>(status, data);
    }

    public static <T> Response<T> of(T data) {
        return new Response<>(HttpStatus.OK.value(), data);
    }
}
