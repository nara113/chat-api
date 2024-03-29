package chat.api.openchat.entity;

import chat.api.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OpenChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "open_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "open_room_id")
    private OpenChatRoom openChatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private OpenChatGroup(OpenChatRoom openChatRoom, User user) {
        this.openChatRoom = openChatRoom;
        this.user = user;
    }

    public static OpenChatGroup createOpenChatGroup(OpenChatRoom chatRoom, User user) {
        return new OpenChatGroup(chatRoom, user);
    }
}
