package chat.api.event;

import chat.api.entity.ChatGroup;
import chat.api.model.ChatMessageDto;
import chat.api.repository.ChatGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PresenceEventListener {

    private final ChatGroupRepository chatGroupRepository;

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Optional.ofNullable(headerAccessor.getSessionAttributes())
                .map(attributes -> (ChatMessageDto) attributes.get("chatMessage"))
                .ifPresent(chatMessage -> {
                    ChatGroup chatGroup = chatGroupRepository.findByChatRoomIdAndUserId(chatMessage.getRoomId(), chatMessage.getSenderId())
                            .orElse(null);

                    chatGroup.changeLastReadMessageId(null);
                });
    }
}