package chat.api.user.dto;

import chat.api.user.entity.Gender;
import chat.api.user.entity.User;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
@Alias("userDto")
public class UserDto {
    private Long userId;
    private String email;
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String statusMessage;
    private String profileUrl;

    public UserDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.gender = user.getGender();
        this.dateOfBirth = user.getDateOfBirth();
        this.statusMessage = user.getStatusMessage();
        this.profileUrl = user.getProfileImage() != null ? user.getProfileImage().getUrl() : null;
    }
}
