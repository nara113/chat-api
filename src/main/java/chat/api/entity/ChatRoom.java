package chat.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(of = {"id", "name"})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatGroup> users = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> messages = new ArrayList<>();
}
