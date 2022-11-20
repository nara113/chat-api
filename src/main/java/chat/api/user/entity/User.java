package chat.api.user.entity;

import chat.api.profile.entity.UploadFile;
import chat.api.common.entity.base.BaseTimeEntity;
import chat.api.room.entity.ChatGroup;
import chat.api.room.entity.ChatMessage;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private UploadFile profileImage;

    private String statusMessage;

    @OneToMany(mappedBy = "user")
    private final List<ChatGroup> groups = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<ChatMessage> message = new ArrayList<>();

    public User(String email, String name, String password, Gender gender, LocalDate dateOfBirth, String statusMessage) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.statusMessage = statusMessage;
    }

    public static User createUser(String email, String name, String password, Gender gender, LocalDate dateOfBirth, String statusMessage) {
        return new User(email, name, password, gender, dateOfBirth, statusMessage);
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
