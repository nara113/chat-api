package chat.api.friend.repository;

import chat.api.friend.entity.ChatFriend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatFriendRepository extends JpaRepository<ChatFriend, Long>, ChatFriendRepositoryCustom {
}
