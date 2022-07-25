package chat.api.event;

import chat.api.model.ChatMessageDto;
import chat.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@RequiredArgsConstructor
//@Component
public class PresenceEventListener {

    private final ChatService chatService;

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Optional.ofNullable(headerAccessor.getSessionAttributes())
                .map(attributes -> (ChatMessageDto) attributes.get("chatMessage"))
                .ifPresent(message -> chatService.updateToLastMessage(message.getRoomId(), message.getSenderId()));
    }
}