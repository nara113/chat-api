package chat.api.room.dto.request;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class CreateRoomRequest {
    @NotBlank
    @Length(max = 50)
    private String roomName;

    @Size(min = 2)
    @NotEmpty
    private List<Long> participantUserIds;
}
