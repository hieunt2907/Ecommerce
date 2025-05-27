package com.shino.ecommerce.common.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LogProducer {
    private static final Logger logger = LoggerFactory.getLogger(LogProducer.class);
    private final KafkaTemplate<String, LogDTO> kafkaTemplate;

    @Value("${spring.kafka.topic.log}")
    private String logTopic;

    public LogProducer(KafkaTemplate<String, LogDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLog(LogDTO logDTO) {
        try {
            kafkaTemplate.send(logTopic, logDTO);
        } catch (Exception e) {
            logger.error("Error sending log to Kafka: {}", e.getMessage(), e);
        }
    }
}
