package chat.api.openchat.entity;

import chat.api.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(of = {"id", "name", "numberOfParticipants", "maxNumberOfParticipants"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OpenChatRoom {
    private static final int INITIAL_NUMBER_OF_PARTICIPANTS = 0;
    private static final int DEFAULT_MAX_NUMBER_OF_PARTICIPANTS = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "open_room_id")
    private Long id;

    @Column(name = "open_room_name", nullable = false)
    private String name;

    private Integer numberOfParticipants;

    private Integer maxNumberOfParticipants;

    @OneToMany(mappedBy = "openChatRoom", cascade = CascadeType.ALL)
    private List<OpenChatGroup> openChatGroups = new ArrayList<>();

    @Version
    private long version;

    public OpenChatRoom(String name, Integer numberOfParticipants, Integer maxNumberOfParticipants) {
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
        this.maxNumberOfParticipants = maxNumberOfParticipants;
    }

    public static OpenChatRoom createOpenChatRoom(String name) {
        return new OpenChatRoom(name, INITIAL_NUMBER_OF_PARTICIPANTS, DEFAULT_MAX_NUMBER_OF_PARTICIPANTS);
    }

    public static OpenChatRoom createOpenChatRoom(String name, Integer maxNumberOfParticipants) {
        return new OpenChatRoom(name, INITIAL_NUMBER_OF_PARTICIPANTS, maxNumberOfParticipants);
    }

    public void joinOpenChatRoom(User user) {
        if (isChatRoomFull()) {
            throw new IllegalArgumentException("open chat room is full.");
        }

        openChatGroups.add(OpenChatGroup.createOpenChatGroup(this, user));

        increaseNumberOfParticipants();
    }

    private void increaseNumberOfParticipants() {
        numberOfParticipants = numberOfParticipants + 1;
    }

    private boolean isChatRoomFull() {
        return numberOfParticipants >= maxNumberOfParticipants;
    }
}
