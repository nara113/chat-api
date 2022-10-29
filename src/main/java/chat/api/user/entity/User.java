package chat.api.user.entity;

import chat.api.profile.entity.UploadFile;
import chat.api.common.entity.base.BaseTimeEntity;
import chat.api.room.entity.ChatGroup;
import chat.api.room.entity.ChatMessage;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private UploadFile profileImage;

    private String statusMessage;

    @OneToMany(mappedBy = "user")
    private List<ChatGroup> groups = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChatMessage> message = new ArrayList<>();

    private User(String email, String name, String password, String statusMessage) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.statusMessage = statusMessage;
    }

    public static User createUser(String email, String name, String password, String statusMessage) {
        return new User(email, name, password, statusMessage);
    }

    public static User createUser(String email, String name, String password) {
        return new User(email, name, password, null);
    }

    public void changeProfileImage(UploadFile uploadFile) {
        if (isProfileImageExist()) {
            profileImage.deleteFile();
        }
        this.profileImage = uploadFile;
    }

    private boolean isProfileImageExist() {
        return profileImage != null;
    }
}
