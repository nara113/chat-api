package chat.api.chat.repository;

import chat.api.chat.entity.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {
    Optional<List<ChatGroup>> findAllByUserId(Long userId);
}
