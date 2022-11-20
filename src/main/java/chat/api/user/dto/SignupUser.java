package chat.api.user.dto;

import chat.api.user.entity.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ToString
@Builder
@Getter @Setter
public class SignupUser {

    @NotBlank(message = "email must not be blank")
    private String email;

    @NotBlank(message = "password must not be blank")
    private String password;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDate dateOfBirth;

    private String statusMessage;
}
