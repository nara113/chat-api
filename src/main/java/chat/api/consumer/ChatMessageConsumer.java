package chat.api.consumer;

import chat.api.model.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatMessageConsumer {
    @KafkaListener(topics = "topic", containerFactory = "ChatMessageDtoKafkaListenerContainerFactory")
    public void test(ChatMessageDto chatMessageDto) {
        log.info("chatMessageDto : {}", chatMessageDto);
    }
}
