package chat.api.openchat.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OpenChatRoom {
    private static final int DEFAULT_MAX_NUMBER_OF_PARTICIPANTS = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "open_room_id")
    private Long id;

    @Column(name = "open_room_name", nullable = false)
    private String name;

    @Max(value = 1500)
    private Integer maxNumberOfParticipants;

    private OpenChatRoom(String name, Integer maxNumberOfParticipants) {
        this.name = name;
        this.maxNumberOfParticipants = maxNumberOfParticipants;
    }

    public static OpenChatRoom createOpenChatRoom(String name) {
        return new OpenChatRoom(name, DEFAULT_MAX_NUMBER_OF_PARTICIPANTS);
    }

    public static OpenChatRoom createOpenChatRoom(String name, Integer maxNumberOfParticipants) {
        return new OpenChatRoom(name, maxNumberOfParticipants);
    }
}
