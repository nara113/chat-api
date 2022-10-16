package chat.api.message.dto;

import chat.api.message.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("chatMessageDto")
public class ChatMessageDto {
    private ChatType chatType;
    private long roomId;
    private long senderId;
    private long messageId;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ChatMessageDto(ChatMessage chatMessage) {
        this.roomId = chatMessage.getChatRoom().getId();
        this.senderId = chatMessage.getUser().getId();
        this.messageId = chatMessage.getId();
        this.message = chatMessage.getMessage();
        this.timestamp = chatMessage.getCreatedDate();
    }
}
