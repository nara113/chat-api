package chat.api.openchat.service;

import chat.api.openchat.dto.OpenChatRoomDto;
import chat.api.openchat.dto.request.CreateOpenChatRequest;
import chat.api.room.entity.ChatMessage;
import chat.api.room.entity.ChatRoom;
import chat.api.room.repository.ChatMessageRepository;
import chat.api.room.repository.ChatRoomRepository;
import chat.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OpenChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Long createOpenChatRoom(CreateOpenChatRequest openChatRequest, User user) {
        final ChatRoom openChatRoom = ChatRoom.createOpenChatRoom(openChatRequest.getRoomName());
        openChatRoom.joinChatRoom(user);

        chatRoomRepository.save(openChatRoom);

        final String invitationMessage = createMessage(user.getName());

        ChatMessage chatMessage = ChatMessage.createJoinMessage(
                invitationMessage,
                user,
                openChatRoom
        );

        chatMessageRepository.save(chatMessage);

        return openChatRoom.getId();
    }

    private String createMessage(String creator) {
        return creator + "님이 들어왔습니다.";
    }

    public List<OpenChatRoomDto> searchOpenChatRooms(String searchText) {
        return chatRoomRepository.findOpenChatRoomByName(searchText)
                .stream()
                .map(OpenChatRoomDto::new)
                .toList();
    }

    @Retryable(value = ObjectOptimisticLockingFailureException.class, backoff = @Backoff(delay = 200))
    @Transactional
    public void joinOpenChatRoom(Long roomId, User user) {
        chatRoomRepository.findOpenChatRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("open chat room does not exist. room id: " + roomId))
                .joinChatRoom(user);
    }
}
