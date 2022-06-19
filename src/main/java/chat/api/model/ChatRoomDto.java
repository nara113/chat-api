package chat.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ChatRoomDto {

    private Long roomId;
    private String roomName;
    private List<UserDto> users;
}