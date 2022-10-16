package chat.api.user.dto;

import chat.api.user.entity.User;
import lombok.*;
import org.apache.ibatis.type.Alias;

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
    private String profileUrl;
    private String statusMessage;

    public UserDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.statusMessage = user.getStatusMessage();
        this.profileUrl = user.getProfileImage() != null ? user.getProfileImage().getUrl() : null;
    }
}