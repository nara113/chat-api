package chat.api.model;

import lombok.*;
import org.apache.ibatis.type.Alias;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Alias("chatEnterDto")
public class ChatEnterDto {
    private long roomId;
    private long userId;
}
