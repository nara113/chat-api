package chat.api.room.entity;

import chat.api.user.entity.User;
import chat.api.message.dto.ChatType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString(of = {"id", "message", "chatType", "createdDate"})
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Lob
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @CreatedDate
    private LocalDateTime createdDate;

    public static ChatMessage createMessage(String messageContent,
                                            User user,
                                            ChatRoom chatRoom,
                                            ChatType chatType) {
        ChatMessage message = new ChatMessage();
        message.message = messageContent;
        message.user = user;
        message.chatRoom = chatRoom;
        message.chatType = chatType;
        return message;
    }
}
