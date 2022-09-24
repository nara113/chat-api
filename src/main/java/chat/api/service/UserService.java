package chat.api.service;

import chat.api.entity.User;
import chat.api.jwt.TokenProvider;
import chat.api.model.Login;
import chat.api.model.SignupUser;
import chat.api.model.TokenConst;
import chat.api.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.concurrent.TimeUnit;

import static chat.api.entity.QUploadFile.uploadFile;
import static chat.api.entity.QUser.user;

@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    private final StringRedisTemplate redisTemplate;

    private final long tokenValidityInMilliseconds;

    private final JPAQueryFactory query;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManagerBuilder authenticationManagerBuilder,
            TokenProvider tokenProvider,
            StringRedisTemplate redisTemplate,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            EntityManager em
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000L;
        this.query = new JPAQueryFactory(em);
    }

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

    public User getUserByEmail(String email) {
        return query
                .selectFrom(user)
                .leftJoin(user.profileImage, uploadFile).fetchJoin()
                .where(user.email.eq(email))
                .fetchOne();
    }

    public void logout(String token) {
        redisTemplate.opsForValue()
                .set(TokenConst.LOGOUT_PREFIX + token, "true", tokenValidityInMilliseconds, TimeUnit.MILLISECONDS);
    }
}
