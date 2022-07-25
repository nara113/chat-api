package chat.api.repository;

import chat.api.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomIdOrderById(Long chatRoomId);

    @Query("select max(m.id) from ChatMessage m where m.chatRoom.id = :roomId")
    Optional<Long> findLastMessageByRoomId(Long roomId);
}
