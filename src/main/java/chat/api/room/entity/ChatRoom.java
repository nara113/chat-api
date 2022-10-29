package chat.api.room.entity;

import chat.api.user.entity.User;
import chat.api.common.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(of = {"id", "name"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatGroup> groups = new ArrayList<>();

    private ChatRoom(String name) {
        this.name = name;
    }

    public static ChatRoom createChatRoom(String name) {
        return new ChatRoom(name);
    }

    public void addUsers(List<User> users) {
        users.forEach(user -> {
            groups.add(ChatGroup.createChatGroup(user, this));
        });
    }
}
