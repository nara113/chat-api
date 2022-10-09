package chat.api.service;

import chat.api.entity.User;
import chat.api.model.UserDto;
import chat.api.repository.ChatFriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
                .collect(toList());
    }
}
