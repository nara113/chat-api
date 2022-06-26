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
    private Long roomId;
    private Long senderId;
    private Long messageId;
    private String message;
}
