package chat.api.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseData {
    private String token;
    private UserDto user;
}
