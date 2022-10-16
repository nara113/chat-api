package chat.api.message.controller;

import chat.api.message.dto.ChatMessageDto;
import chat.api.message.dto.ReadMessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final String messageTopic;

    private final String readTopic;

    private final String enterTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MessageController(@Value("${spring.kafka.topic.message-topic}") String messageTopic,
                             @Value("${spring.kafka.topic.read-topic}") String readTopic,
                             @Value("${spring.kafka.topic.enter-topic}") String enterTopic,
                             KafkaTemplate<String, Object> kafkaTemplate) {
        this.messageTopic = messageTopic;
        this.readTopic = readTopic;
        this.enterTopic = enterTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @MessageMapping("/chat/message/users")
    public void sendToUsers(ChatMessageDto chatMessage) {
        kafkaTemplate.send(messageTopic, chatMessage);
    }

    @MessageMapping("/chat/message/read")
    public void readMessage(ReadMessageDto readMessageDto) {
        kafkaTemplate.send(readTopic, readMessageDto);
    }

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        kafkaTemplate.send(enterTopic, chatMessage);
    }
}