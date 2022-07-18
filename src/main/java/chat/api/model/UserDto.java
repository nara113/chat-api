package chat.api.model;

import chat.api.entity.User;
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

    public UserDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
