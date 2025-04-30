package com.clone.linkedin.connections_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic sendConnectionRequestTopic() {
        return new NewTopic("send-connection-request-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic acceptConnectionRequestTopic() {
        return new NewTopic("accept-connection-request-topic", 3, (short) 1);
    }
}
