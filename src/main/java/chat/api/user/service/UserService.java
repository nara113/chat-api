package chat.api.user.service;

import chat.api.user.entity.User;
import chat.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmailWithProfile(email);
    }
}
