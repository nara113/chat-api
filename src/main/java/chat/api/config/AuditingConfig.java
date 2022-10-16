package chat.api.config;

import chat.api.util.SecurityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@EnableJpaAuditing
@Configuration
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return SecurityUtil::getCurrentUserEmail;
    }
}
