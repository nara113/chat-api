package chat.api.queue.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaFailureCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void produce(String topic, Object data) {
        kafkaTemplate.send(topic, data)
                .addCallback(result -> {
                }, (KafkaFailureCallback<String, Object>) ex -> {
                    ProducerRecord<String, Object> failed = ex.getFailedProducerRecord();

                    log.error("kafka send fail. topic: " + failed.topic() + ", value: " + failed.value());
                });
    }
}
