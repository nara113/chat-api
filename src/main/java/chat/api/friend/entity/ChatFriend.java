package chat.api.friend.entity;

import chat.api.user.entity.User;
import chat.api.entity.base.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
public class ChatFriend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_friend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_user_id")
    private User friend;

    @ColumnDefault("'N'")
    private boolean isBlocked;

    @Builder
    private ChatFriend(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }
}
