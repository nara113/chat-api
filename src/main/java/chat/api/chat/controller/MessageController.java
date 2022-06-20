package chat.api.chat.controller;

import chat.api.chat.model.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/chat/message")
    public void send(ChatMessageDto messageDto) {

        template.convertAndSend("/topic/chat/room/" + messageDto.getRoomId(), messageDto);
    }
}