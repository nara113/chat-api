package chat.api.model;

import chat.api.entity.ChatGroup;
import lombok.Data;

@Data
public class LastReadMessageDto {
    private Long userId;
    private Long lastReadMessageId;

    public LastReadMessageDto(ChatGroup chatGroup) {
        this.userId = chatGroup.getUser().getId();
        this.lastReadMessageId = chatGroup.getLastReadMessageId();
    }
}
