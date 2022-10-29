package chat.api.room.entity;

import chat.api.message.dto.ChatType;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@ToString
@Getter
@Document(collection = "messages")
public class Message {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;

    private final Long senderId;

    private final Long roomId;

    private final String message;

    private final ChatType chatType;

    @CreatedDate
    private LocalDateTime timestamp;

    private Message(Long senderId, Long roomId, String message, ChatType chatType) {
        this.senderId = senderId;
        this.roomId = roomId;
        this.message = message;
        this.chatType = chatType;
    }

    public static Message createMessage(String message,
                                        Long senderId,
                                        Long roomId,
                                        ChatType chatType) {
        return new Message(senderId, roomId, message, chatType);
    }
}
