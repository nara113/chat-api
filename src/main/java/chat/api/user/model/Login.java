package chat.api.user.model;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class Login {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
