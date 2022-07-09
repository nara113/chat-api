package chat.api.chat.model;

import lombok.*;
import org.apache.ibatis.type.Alias;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Alias("chatMessageDto")
public class ChatMessageDto {
    private long roomId;
    private long senderId;
    private long messageId;
    private String message;
}
