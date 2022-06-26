package chat.api.chat.entity;

import chat.api.user.entity.User;
import lombok.*;

import javax.persistence.*;

@ToString
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatGroup extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    private Long lastReadMessageId;
}
