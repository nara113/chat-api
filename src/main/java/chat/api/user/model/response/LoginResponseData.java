package chat.api.user.model.response;

import chat.api.user.model.UserDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseData {
    private String token;
    private UserDto user;
}
