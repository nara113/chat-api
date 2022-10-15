package chat.api.entity;

import chat.api.model.ChatType;
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

    private Long senderId;

    private Long roomId;

    private String message;

    private ChatType chatType;

    @CreatedDate
    private LocalDateTime timestamp;

    public static Message createMessage(String messageContent,
                                        Long senderId,
                                        Long roomId,
                                        ChatType chatType) {
        Message message = new Message();
        message.message = messageContent;
        message.senderId = senderId;
        message.roomId = roomId;
        message.chatType = chatType;
        return message;
    }
}
