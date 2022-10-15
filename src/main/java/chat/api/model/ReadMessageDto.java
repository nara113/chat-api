package chat.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadMessageDto {
    private Long roomId;
    private Long userId;
    private Long lastMessageId;
}
