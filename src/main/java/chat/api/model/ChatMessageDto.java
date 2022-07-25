package chat.api.model;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Alias("chatMessageDto")
public class ChatMessageDto {
    private ChatType chatType;
    private long roomId;
    private long senderId;
    private long messageId;
    private String message;
    private LocalDateTime createdDate = LocalDateTime.now();
}
