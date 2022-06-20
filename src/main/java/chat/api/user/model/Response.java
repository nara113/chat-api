package chat.api.user.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Response<T> {
    private int status;

    private String message;

    private T data;
}
