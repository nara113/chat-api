package chat.api.controller;

import chat.api.entity.User;
import chat.api.model.ChatMessageDto;
import chat.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate template;

    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void send(ChatMessageDto chatMessage) {
        long messageId = chatService.saveChatMessage(chatMessage);
        chatMessage.setMessageId(messageId);

        template.convertAndSend("/topic/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }

    @MessageMapping("/chat/message/users")
    public void sendToUsers(ChatMessageDto chatMessage) {
        long messageId = chatService.saveChatMessage(chatMessage);
        chatMessage.setMessageId(messageId);

        List<User> roomUsers = chatService.getUsersByRoomId(chatMessage.getRoomId());

        roomUsers.forEach(user -> template.convertAndSend("/queue/user/" + user.getId(), chatMessage));
    }

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("chatMessage", chatMessage);
    }
}