package chat.api.room.entity;

import chat.api.user.entity.User;
import chat.api.common.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(of = {"id", "name"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends BaseEntity {
    private static final int DEFAULT_MAX_NUMBER_OF_PARTICIPANTS = 100;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private final List<ChatGroup> groups = new ArrayList<>();

    private int numberOfParticipants;

    private int maxNumberOfParticipants;

    private boolean isOpenChatRoom;

    @Version
    private long version;

    public ChatRoom(String name, int numberOfParticipants, int maxNumberOfParticipants, boolean isOpenChatRoom) {
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
        this.maxNumberOfParticipants = maxNumberOfParticipants;
        this.isOpenChatRoom = isOpenChatRoom;
    }

    public static ChatRoom createChatRoom(String name) {
        return new ChatRoom(name, 0, DEFAULT_MAX_NUMBER_OF_PARTICIPANTS, false);
    }

    public static ChatRoom createChatRoom(String name, int numberOfParticipants) {
        return new ChatRoom(name, numberOfParticipants, DEFAULT_MAX_NUMBER_OF_PARTICIPANTS, false);
    }

    public static ChatRoom createOpenChatRoom(String name) {
        return new ChatRoom(name, 0, DEFAULT_MAX_NUMBER_OF_PARTICIPANTS, true);
    }

    public static ChatRoom createOpenChatRoom(String name, Integer maxNumberOfParticipants) {
        return new ChatRoom(name, 0, maxNumberOfParticipants, true);
    }

    public void joinChatRoom(User user) {
        if (isChatRoomFull()) {
            throw new IllegalArgumentException("open chat room is full.");
        }
        increaseNumberOfParticipants();
        groups.add(ChatGroup.createChatGroup(this, user));
    }

    private void increaseNumberOfParticipants() {
        numberOfParticipants = numberOfParticipants + 1;
    }

    private boolean isChatRoomFull() {
        return numberOfParticipants >= maxNumberOfParticipants;
    }
}
