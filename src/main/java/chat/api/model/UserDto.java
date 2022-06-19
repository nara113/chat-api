package chat.api.model;

import lombok.*;
import org.apache.ibatis.type.Alias;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
@Alias("user")
public class UserDto {
    private String email;
    private String name;
}
