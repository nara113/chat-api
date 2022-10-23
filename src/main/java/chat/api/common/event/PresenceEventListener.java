package chat.api.common.event;

import chat.api.message.dto.ChatMessageDto;
import chat.api.room.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@RequiredArgsConstructor
//@Component
public class PresenceEventListener {

    private final ChatRoomService chatRoomService;

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Optional.ofNullable(headerAccessor.getSessionAttributes())
                .map(attributes -> (ChatMessageDto) attributes.get("chatMessage"))
                .ifPresent(message -> chatRoomService.updateToLastMessage(message.getRoomId(), message.getSenderId()));
    }
}