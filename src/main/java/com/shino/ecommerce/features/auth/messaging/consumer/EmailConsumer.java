package com.shino.ecommerce.features.auth.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.shino.ecommerce.features.auth.dto.EmailMessageDTO;
import com.shino.ecommerce.features.auth.service.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void consumeMessage(EmailMessageDTO emailMessage) {
        emailService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getText());
    }
}
