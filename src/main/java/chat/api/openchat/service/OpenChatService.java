package chat.api.openchat.service;

import chat.api.openchat.dto.OpenChatRoomDto;
import chat.api.openchat.dto.request.CreateOpenChatRequest;
import chat.api.openchat.entity.OpenChatRoom;
import chat.api.openchat.repository.OpenChatRoomRepository;
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
    private final OpenChatRoomRepository openChatRoomRepository;

    @Transactional
    public Long createOpenChatRoom(CreateOpenChatRequest openChatRequest, User user) {
        OpenChatRoom openChatRoom = OpenChatRoom.createOpenChatRoom(openChatRequest.getRoomName());
        openChatRoom.joinOpenChatRoom(user);

        openChatRoomRepository.save(openChatRoom);

        return openChatRoom.getId();
    }

    public List<OpenChatRoomDto> searchOpenChatRooms(String searchText) {
        return openChatRoomRepository.findByNameContainsIgnoreCase(searchText)
                .stream()
                .map(OpenChatRoomDto::new)
                .toList();
    }

    @Retryable(value = ObjectOptimisticLockingFailureException.class, backoff = @Backoff(delay = 200))
    @Transactional
    public void joinOpenChatRoom(Long roomId, User user) {
        OpenChatRoom openChatRoom = openChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("open chat room does not exist. room id: " + roomId));

        openChatRoom.joinOpenChatRoom(user);
    }
}
