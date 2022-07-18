package chat.api.entity;

import chat.api.entity.BaseEntity;
import chat.api.entity.ChatGroup;
import chat.api.entity.ChatMessage;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(of = {"id", "email", "name", "password"})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private String profileUrl;

    @OneToMany(mappedBy = "user")
    private List<ChatGroup> groups = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChatMessage> message = new ArrayList<>();
}
