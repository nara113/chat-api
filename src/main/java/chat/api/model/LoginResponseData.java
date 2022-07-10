package chat.api.model;

import chat.api.model.UserDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseData {
    private String token;
    private UserDto user;
}
