package chat.api.queue.kafka;

import chat.api.common.interceptor.MessageDestination;
import chat.api.message.dto.ChatMessageDto;
import chat.api.message.dto.ReadMessageDto;
import chat.api.room.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ChatRoomService chatRoomService;

    private final SimpMessagingTemplate template;

    @KafkaListener(topics = "${spring.kafka.topic.message-topic}", containerFactory = "ChatMessageDtoKafkaListenerContainerFactory")
    public void sendMessage(ChatMessageDto chatMessage) {
        log.info("sendMessage : {}", chatMessage);

        Long messageId = chatRoomService.saveChatMessage(chatMessage);
        chatMessage.setMessageId(messageId);

        chatRoomService.getGroupsByRoomId(chatMessage.getRoomId())
                .forEach(group ->
                        template.convertAndSend(MessageDestination.USER.getPrefix() + group.getUser().getId(), chatMessage));
    }

    @KafkaListener(topics = "${spring.kafka.topic.read-topic}", containerFactory = "ChatMessageDtoKafkaListenerContainerFactory")
    public void read(ReadMessageDto readMessageDto) {
        log.info("read : {}", readMessageDto);

        chatRoomService.markAsRead(readMessageDto);

        template.convertAndSend(MessageDestination.ROOM.getPrefix() + readMessageDto.getRoomId(), readMessageDto);
    }

    @KafkaListener(topics = "${spring.kafka.topic.enter-topic}", containerFactory = "ChatMessageDtoKafkaListenerContainerFactory")
    public void enter(ChatMessageDto chatMessage) {
        log.info("enter : {}", chatMessage);

        Long lastMessageId = chatRoomService.updateToLastMessage(chatMessage.getRoomId(), chatMessage.getSenderId());

        ReadMessageDto readMessageDto = ReadMessageDto.builder()
                .lastReadMessageId(lastMessageId)
                .roomId(chatMessage.getRoomId())
                .userId(chatMessage.getSenderId())
                .build();

        template.convertAndSend(MessageDestination.ROOM.getPrefix() + chatMessage.getRoomId(), readMessageDto);
    }
}
