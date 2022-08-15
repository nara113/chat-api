package chat.api.service;

import chat.api.entity.User;
import chat.api.jwt.TokenProvider;
import chat.api.model.Login;
import chat.api.model.SignupUser;
import chat.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    @Transactional
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

    public String getToken(Login login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

        Authentication authenticate = authenticationManagerBuilder
                .getObject()
                .authenticate(usernamePasswordAuthenticationToken);

        return tokenProvider.createToken(authenticate);
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("user does not exist. user email : " + email));
    }
}
