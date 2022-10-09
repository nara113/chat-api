package chat.api.repository;

import chat.api.entity.User;

import java.util.List;

public interface ChatFriendRepositoryCustom {
    List<User> selectFriendsByUserId(Long userId);
}
