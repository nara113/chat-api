package chat.api.chat.model;

import chat.api.user.model.UserDto;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Alias("chatRoomDto")
public class ChatRoomDto {
    private Long roomId;
    private String roomName;
    private List<UserDto> users;
}