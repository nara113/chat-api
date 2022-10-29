package chat.api.openchat.service;

import chat.api.openchat.dto.request.CreateOpenChatRequest;
import chat.api.openchat.entity.OpenChatRoom;
import chat.api.openchat.repository.OpenChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OpenChatService {
    private final OpenChatRoomRepository openChatRoomRepository;

    @Transactional
    public Long createOpenChatRoom(CreateOpenChatRequest request) {
        OpenChatRoom openChatRoom = OpenChatRoom.createOpenChatRoom(request.getRoomName());
        openChatRoomRepository.save(openChatRoom);
        return openChatRoom.getId();
    }

    public List<OpenChatRoom> searchOpenChat(String searchText) {
        return openChatRoomRepository.findByNameContainsIgnoreCase(searchText);
    }
}
