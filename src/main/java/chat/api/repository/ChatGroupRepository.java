package chat.api.repository;

import chat.api.entity.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {
    Optional<ChatGroup> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    @Query("select g from ChatGroup g join fetch g.user where g.chatRoom.id = :chatRoomId")
    List<ChatGroup> findByChatRoomId(Long chatRoomId);
}
