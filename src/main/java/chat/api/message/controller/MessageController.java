package chat.api.message.controller;

import chat.api.message.dto.ChatMessageDto;
import chat.api.message.dto.ReadMessageDto;
import chat.api.messagebroker.kafka.KafkaProducer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MessageController {
    private final String messageTopic;

    private final String readTopic;

    private final String enterTopic;

    private final KafkaProducer kafkaProducer;

    public MessageController(@Value("${topic.message-topic}") String messageTopic,
                             @Value("${topic.read-topic}") String readTopic,
                             @Value("${topic.enter-topic}") String enterTopic,
                             KafkaProducer kafkaProducer) {
        this.messageTopic = messageTopic;
        this.readTopic = readTopic;
        this.enterTopic = enterTopic;
        this.kafkaProducer = kafkaProducer;
    }

    @MessageMapping("/chat/message/users")
    public void sendToUsers(@Valid ChatMessageDto chatMessage) {
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