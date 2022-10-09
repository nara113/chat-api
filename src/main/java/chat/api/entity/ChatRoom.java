package chat.api.entity;

import chat.api.entity.base.BaseEntity;
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

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatGroup> groups = new ArrayList<>();

    @Builder
    private ChatRoom(String name) {
        this.name = name;
    }

    public void addUsers(List<User> users) {
        users.forEach(user -> {
            groups.add(ChatGroup.builder().user(user).chatRoom(this).build());
        });
    }
}
