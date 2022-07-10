package chat.api.controller;

import chat.api.model.ChatEnterDto;
import chat.api.model.ChatMessageDto;
import chat.api.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/chats")
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate template;

    private final ChatService chatService;

    @Operation(summary = "채팅방 가져오기")
    @MessageMapping("/chat/message")
    public void send(ChatMessageDto messageDto) {
        long messageId = chatService.saveChatMessage(messageDto);
        messageDto.setMessageId(messageId);

        template.convertAndSend("/topic/chat/room/" + messageDto.getRoomId(), messageDto);
    }
}