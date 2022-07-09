package chat.api.chat.repository;

import chat.api.chat.entity.ChatFriend;
import chat.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatFriendRepository extends JpaRepository<ChatFriend, Long> {
    Optional<List<ChatFriend>> findByUserAndBlockYn(User user, String blockYn);
}
