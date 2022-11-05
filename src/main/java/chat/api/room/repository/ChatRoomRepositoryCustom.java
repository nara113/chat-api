package chat.api.room.repository;

import chat.api.room.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> findOpenChatRoomByName(String searchText);
    Optional<ChatRoom> findOpenChatRoomById(Long chatRoomId);
}
