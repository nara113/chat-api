package chat.api.repository;

import chat.api.entity.ChatFriend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatFriendRepository extends JpaRepository<ChatFriend, Long>, ChatFriendRepositoryCustom {
}
