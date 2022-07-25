package chat.api.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReadMessageDto {
    private long roomId;
    private long userId;
    private long messageId;
}
