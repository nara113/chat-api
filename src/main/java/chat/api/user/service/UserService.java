package chat.api.user.service;

import chat.api.user.entity.User;
import chat.api.user.model.SignupUser;
import chat.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void signup(SignupUser signupUser) {
        userRepository.findByEmail(signupUser.getEmail()).ifPresent(email -> {
            throw new IllegalStateException("email already exists.");
        });

        User user = User.builder()
                .email(signupUser.getEmail())
                .password(passwordEncoder.encode(signupUser.getPassword()))
                .name(signupUser.getName())
                .build();

        userRepository.save(user);
    }
}
