package chat.api.openchat.dto;

import chat.api.openchat.entity.OpenChatRoom;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class OpenChatRoomDto {
    private final String name;
    private final Integer numberOfParticipants;
    private final Integer maxNumberOfParticipants;

    public OpenChatRoomDto(OpenChatRoom openChatRoom) {
        this.name = openChatRoom.getName();
        this.numberOfParticipants = openChatRoom.getNumberOfParticipants();
        this.maxNumberOfParticipants = openChatRoom.getMaxNumberOfParticipants();
    }
}
