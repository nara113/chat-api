package chat.api.openchat.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateOpenChatRequest {
    @NotBlank
    private String roomName;
}
