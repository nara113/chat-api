package chat.api.messagebroker.kafka;

import chat.api.common.interceptor.MessageDestination;
import chat.api.message.dto.ChatMessageDto;
import chat.api.message.dto.ReadMessageDto;
import chat.api.room.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate template;

    @KafkaListener(
            topics = "${topic.message-topic}",
            containerFactory = "kafkaListenerFactory"
    )
    public void sendMessage(List<ChatMessageDto> chatMessages) {
        chatMessages.forEach(chatMessage -> {
            try {
                log.info("sendMessage : {}", chatMessage);

                Long messageId = chatRoomService.saveChatMessage(chatMessage);
                chatMessage.setMessageId(messageId);

                chatRoomService.getGroupsByRoomId(chatMessage.getRoomId()).forEach(group ->
                        template.convertAndSend(MessageDestination.USER.getPrefix() + group.getUser().getId(), chatMessage));

            } catch (Exception e) {
                log.error("kafka consumer enter error. chatMessage: {}", chatMessage);
            }
        });
    }

    @KafkaListener(
            topics = "${topic.read-topic}",
            containerFactory = "kafkaListenerFactory"
    )
    public void read(List<ReadMessageDto> readMessages) {
        readMessages.forEach(readMessage -> {
            try {
                log.info("read : {}", readMessage);

                chatRoomService.markAsRead(readMessage);

                template.convertAndSend(MessageDestination.ROOM.getPrefix() + readMessage.getRoomId(), readMessage);
            } catch (Exception e) {
                log.error("kafka consumer read error. readMessage: {}", readMessage);
            }
        });
    }

    @KafkaListener(
            topics = "${topic.enter-topic}",
            containerFactory = "kafkaListenerFactory"
    )
    public void enter(List<ChatMessageDto> chatMessages) {
        chatMessages.forEach(chatMessage -> {
            try {
                log.info("enter : {}", chatMessage);

                Long lastMessageId = chatRoomService.updateToLastMessage(chatMessage.getRoomId(), chatMessage.getSenderId());

                ReadMessageDto readMessageDto = ReadMessageDto.builder()
                        .lastReadMessageId(lastMessageId)
                        .roomId(chatMessage.getRoomId())
                        .userId(chatMessage.getSenderId())
                        .build();

                template.convertAndSend(MessageDestination.ROOM.getPrefix() + chatMessage.getRoomId(), readMessageDto);

            } catch (Exception e) {
                log.error("kafka consumer enter error. chatMessage: {}", chatMessage);
            }
        });
    }
}
