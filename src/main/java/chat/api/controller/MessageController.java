package chat.api.controller;

import chat.api.model.ChatMessageDto;
import chat.api.model.ReadMessageDto;
import chat.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

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

        chatService.getGroupsByRoomId(chatMessage.getRoomId())
                .forEach(group -> template.convertAndSend("/queue/user/" + group.getUser().getId(), chatMessage));
    }

    @MessageMapping("/chat/message/read")
    public void readMessage(ReadMessageDto readMessageDto) {
        chatService.markAsRead(readMessageDto);

        template.convertAndSend("/topic/room/" + readMessageDto.getRoomId(), readMessageDto);
    }

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("chatMessage", chatMessage);

        Long lastMessageId = chatService.updateToLastMessage(chatMessage.getRoomId(), chatMessage.getSenderId());

        ReadMessageDto readMessageDto = ReadMessageDto.builder()
                .messageId(lastMessageId)
                .roomId(chatMessage.getRoomId())
                .userId(chatMessage.getSenderId())
                .build();

        template.convertAndSend("/topic/room/" + chatMessage.getRoomId(), readMessageDto);
    }
}