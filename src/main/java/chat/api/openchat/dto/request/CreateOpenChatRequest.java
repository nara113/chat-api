package chat.api.openchat.dto.request;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateOpenChatRequest {
    @NotBlank
    @Length(max = 50)
    private String roomName;
}
