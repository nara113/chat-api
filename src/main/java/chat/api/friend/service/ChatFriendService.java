package chat.api.friend.service;

import chat.api.user.entity.User;
import chat.api.user.dto.UserDto;
import chat.api.friend.repository.ChatFriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatFriendService {

    private final ChatFriendRepository chatFriendRepository;

    public List<UserDto> getFriends(Long userId) {
        List<User> friends = chatFriendRepository.selectFriendsByUserId(userId);

        return friends
                .stream()
                .map(UserDto::new)
                .toList();
    }
}
