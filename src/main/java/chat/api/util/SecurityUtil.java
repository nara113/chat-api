package chat.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtil {
    public static Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return getCurrentUserEmail(authentication);
    }

    public static Optional<String> getCurrentUserEmail(Authentication authentication) {
        if (authentication == null) {
            log.debug("No credentials found.");
            return Optional.empty();
        }

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

        return Optional.ofNullable(userEmail);
    }
}
