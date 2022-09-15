package chat.api.model.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class CreateRoomRequest {
    @NotBlank
    private String roomName;

    @Size(min = 2)
    @NotEmpty
    private List<Long> participantUserIds;
}
