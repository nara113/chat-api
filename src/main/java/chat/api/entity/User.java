package chat.api.entity;

import chat.api.entity.base.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(of = {"id", "email", "name", "password"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

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

    private String statusMessage;

    @OneToMany(mappedBy = "user")
    private List<ChatGroup> groups = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChatMessage> message = new ArrayList<>();

    @Builder
    private User(String email, String name, String password, String profileUrl, String statusMessage) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileUrl = profileUrl;
        this.statusMessage = statusMessage;
    }

    public void changeProfileImageUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
