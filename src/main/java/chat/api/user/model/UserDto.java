package chat.api.user.model;

import lombok.*;
import org.apache.ibatis.type.Alias;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
@Alias("userDto")
public class UserDto {
    private String email;
    private String name;
}
