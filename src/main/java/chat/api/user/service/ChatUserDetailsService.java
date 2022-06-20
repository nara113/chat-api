package chat.api.user.service;

import chat.api.user.model.Authority;
import chat.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByEmail(userId)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist. user id : " + userId));
    }

    private User createUser(chat.api.user.entity.User user) {
        return new User(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(Authority.ROLE_USER.name())));
    }
}


