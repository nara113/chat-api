package chat.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
@Alias("chatRoomDto")
public class ChatRoomDto {
    private Long roomId;
    private String roomName;
    private List<UserDto> users;
    private Long unreadMessagesCount;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
}