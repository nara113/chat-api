package chat.api.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Builder
@Getter @Setter
public class SignupUser {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
