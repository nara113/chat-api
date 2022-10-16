package chat.api.friend.repository;

import chat.api.user.entity.User;

import java.util.List;

public interface ChatFriendRepositoryCustom {
    List<User> selectFriendsByUserId(Long userId);
}
