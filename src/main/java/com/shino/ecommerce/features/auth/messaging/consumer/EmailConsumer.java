package com.shino.ecommerce.features.auth.messaging.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.shino.ecommerce.features.auth.dto.EmailMessageDTO;
import com.shino.ecommerce.features.auth.service.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "${spring.kafka.topic.email}", groupId = "email-group")
    public void consume(EmailMessageDTO emailMessage) {
        emailService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getText());
    }
}
