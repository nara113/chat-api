package chat.api.model;

import chat.api.entity.ChatMessage;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("chatMessageDto")
public class ChatMessageDto {
    private ChatType chatType;
    private long roomId;
    private long senderId;
    private String senderName;
    private long messageId;
    private String message;
    private LocalDateTime createdDate = LocalDateTime.now();

    public ChatMessageDto(ChatMessage chatMessage) {
        this.roomId = chatMessage.getChatRoom().getId();
        this.senderId = chatMessage.getUser().getId();
        this.senderName = chatMessage.getSenderName();
        this.messageId = chatMessage.getId();
        this.message = chatMessage.getMessage();
    }
}
