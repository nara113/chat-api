package chat.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class KafkaConsumerConfig implements KafkaListenerConfigurer {
    private final LocalValidatorFactoryBean validator;

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(validator);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    kafkaListenerFactory(ConsumerFactory<String, Object> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory);
        factory.setConcurrency(5);
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public KafkaListenerErrorHandler kafkaErrorHandler() {
        return (message, exception) -> {
            log.error("kafkaMessage=[{}], error=[{}], header=[{}]",
                    message.getPayload(), exception.getMessage(), message.getHeaders());
            return message.getPayload();
        };
    }
}