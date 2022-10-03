package chat.api.consumer;

import chat.api.model.ChatMessageDto;
import chat.api.model.ReadMessageDto;
import chat.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {
    private final ChatService chatService;

    private final SimpMessagingTemplate template;

    @KafkaListener(topics = "${spring.kafka.topic.message-topic}", containerFactory = "ChatMessageDtoKafkaListenerContainerFactory")
    public void sendMessage(ChatMessageDto chatMessage) {
        log.info("sendMessage : {}", chatMessage);

        long messageId = chatService.saveChatMessage(chatMessage);
        chatMessage.setMessageId(messageId);

        chatService.getGroupsByRoomId(chatMessage.getRoomId())
                .forEach(group ->
                        template.convertAndSend("/queue/user/" + group.getUser().getId(), chatMessage));
    }

    @KafkaListener(topics = "${spring.kafka.topic.read-topic}", containerFactory = "ChatMessageDtoKafkaListenerContainerFactory")
    public void read(ReadMessageDto readMessage) {
        log.info("read : {}", readMessage);

        chatService.markAsRead(readMessage);

        template.convertAndSend("/topic/room/" + readMessage.getRoomId(), readMessage);
    }

    @KafkaListener(topics = "${spring.kafka.topic.enter-topic}", containerFactory = "ChatMessageDtoKafkaListenerContainerFactory")
    public void enter(ChatMessageDto chatMessage) {
        log.info("enter : {}", chatMessage);

        Long lastMessageId = chatService.updateToLastMessage(chatMessage.getRoomId(), chatMessage.getSenderId());

        ReadMessageDto readMessageDto = ReadMessageDto.builder()
                .messageId(lastMessageId)
                .roomId(chatMessage.getRoomId())
                .userId(chatMessage.getSenderId())
                .build();

        template.convertAndSend("/topic/room/" + chatMessage.getRoomId(), readMessageDto);
    }
}
