package chat.api.common.interceptor;

import chat.api.jwt.TokenProvider;
import chat.api.room.repository.ChatGroupRepository;
import chat.api.user.repository.UserRepository;
import chat.api.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TopicSubscriptionInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    private final UserRepository userRepository;

    private final ChatGroupRepository chatGroupRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String jwt = headerAccessor.getFirstNativeHeader("auth-token");
            Authentication authentication = tokenProvider.getAuthentication(jwt);

            if (!isSubscriptionValid(authentication, headerAccessor.getDestination())) {
                throw new IllegalArgumentException("No permission for this topic");
            }
        }

        return message;
    }

    private boolean isSubscriptionValid(Authentication authentication, String topicDestination) {
        if (authentication == null) {
            return false;
        }

        String email = SecurityUtil.getCurrentUserEmail(authentication)
                .orElseThrow(() -> new IllegalArgumentException("authentication is invalid."));

        Long userId = userRepository.findByIdByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("email does not exist. email: " + email));

        MessageDestination messageDestination = MessageDestination.findByPrefix(topicDestination);
        Long id = messageDestination.getId(topicDestination)
                .orElseThrow(() -> new IllegalArgumentException("destination is invalid."));

        return switch (messageDestination) {
            case ROOM -> chatGroupRepository
                    .findByChatRoomIdAndUserId(id, userId)
                    .isPresent();
            case USER -> userId.equals(id);
        };
    }
}