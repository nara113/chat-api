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

    private ChatMessage(String message, User user, ChatRoom chatRoom, ChatType chatType) {
        this.message = message;
        this.user = user;
        this.chatRoom = chatRoom;
        this.chatType = chatType;
    }

    public static ChatMessage createMessage(String message,
                                            User user,
                                            ChatRoom chatRoom,
                                            ChatType chatType) {
        return new ChatMessage(message, user, chatRoom, chatType);
    }
}
