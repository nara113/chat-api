package chat.api.message.controller;

import chat.api.message.dto.ChatMessageDto;
import chat.api.message.dto.ReadMessageDto;
import chat.api.broker.kafka.KafkaProducer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private static final int MAX_MESSAGE_LENGTH = 1_000;

    private final String messageTopic;

    private final String readTopic;

    private final String enterTopic;

    private final KafkaProducer kafkaProducer;

    public MessageController(@Value("${spring.kafka.topic.message-topic}") String messageTopic,
                             @Value("${spring.kafka.topic.read-topic}") String readTopic,
                             @Value("${spring.kafka.topic.enter-topic}") String enterTopic,
                             KafkaProducer kafkaProducer) {
        this.messageTopic = messageTopic;
        this.readTopic = readTopic;
        this.enterTopic = enterTopic;
        this.kafkaProducer = kafkaProducer;
    }

    @MessageMapping("/chat/message/users")
    public void sendToUsers(ChatMessageDto chatMessage) {
        if (StringUtils.length(chatMessage.getMessage()) > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("The message length exceeded 1000 characters.");
        }

        kafkaProducer.produce(messageTopic, chatMessage);
    }

    @MessageMapping("/chat/message/read")
    public void readMessage(ReadMessageDto readMessageDto) {
        kafkaProducer.produce(readTopic, readMessageDto);
    }

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto chatMessage) {
        kafkaProducer.produce(enterTopic, chatMessage);
    }
}