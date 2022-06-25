package chat.api.user.model;

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

    public UserDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
