package chat.api.service;

import chat.api.mapper.ChatMapper;
import chat.api.model.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final ChatMapper chatMapper;

    public List<ChatRoomDto> getAllRoom(Long userId) {
        return chatMapper.selectAllRoomsByUserId(userId);
    }
}
