package chat.api.user.service;

import chat.api.user.entity.User;
import chat.api.jwt.TokenProvider;
import chat.api.user.dto.Login;
import chat.api.user.dto.SignupUser;
import chat.api.common.model.TokenConst;
import chat.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Transactional(readOnly = true)
@Service
public class AuthService {
    private final long tokenValidityInMilliseconds;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    private final StringRedisTemplate redisTemplate;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManagerBuilder authenticationManagerBuilder,
            TokenProvider tokenProvider,
            StringRedisTemplate redisTemplate,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000L;
    }

    @Transactional
    public void signup(SignupUser signupUser) {
        userRepository.findByEmail(signupUser.getEmail()).ifPresent(email -> {
            throw new IllegalStateException("email already exists.");
        });

        User user = User.createUser(signupUser.getEmail(),
                passwordEncoder.encode(signupUser.getPassword()),
                signupUser.getName());

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

    public void logout(String token) {
        redisTemplate.opsForValue()
                .set(TokenConst.LOGOUT_PREFIX + token, "true", tokenValidityInMilliseconds, TimeUnit.MILLISECONDS);
    }
}
