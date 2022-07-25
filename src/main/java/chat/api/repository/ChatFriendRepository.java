package chat.api.repository;

import chat.api.entity.ChatFriend;
import chat.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatFriendRepository extends JpaRepository<ChatFriend, Long> {
    @Query("select f.friend from ChatFriend f where f.user.id = :userId and f.blockYn = :blockYn")
    List<User> findFriendByUserIdAndBlockYn(Long userId, String blockYn);
}
