package chat.api.message.dto;

import chat.api.room.entity.ChatGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadMessageDto {
    private Long roomId;
    private Long userId;
    private Long lastReadMessageId;

    public ReadMessageDto(ChatGroup chatGroup) {
        this.roomId = chatGroup.getChatRoom().getId();
        this.userId = chatGroup.getUser().getId();
        this.lastReadMessageId = chatGroup.getLastReadMessageId();
    }
}
