package chat.api.chat.controller;

import chat.api.chat.model.ChatMessageDto;
import chat.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate template;

    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void send(ChatMessageDto messageDto) {
        long messageId = chatService.saveChatMessage(messageDto);
        messageDto.setMessageId(messageId);

        template.convertAndSend("/topic/chat/room/" + messageDto.getRoomId(), messageDto);
    }
}