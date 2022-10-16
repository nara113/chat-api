package chat.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class Login {
    @Schema(description = "이메일" , example = "email")
    @NotBlank
    private String email;

    @Schema(description = "패스워드" , example = "password")
    @NotBlank
    private String password;
}
