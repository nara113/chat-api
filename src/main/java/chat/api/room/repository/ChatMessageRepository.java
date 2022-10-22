package chat.api.room.repository;

import chat.api.room.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop30ByChatRoomIdOrderByIdDesc(Long chatRoomId);

    List<ChatMessage> findTop30ByChatRoomIdAndIdIsLessThanOrderByIdDesc(Long chatRoomId, Long messageId);

    @Query("select max(m.id) from ChatMessage m where m.chatRoom.id = :roomId")
    Optional<Long> findLastMessageByRoomId(Long roomId);
}
