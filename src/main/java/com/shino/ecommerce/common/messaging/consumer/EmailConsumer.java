package com.shino.ecommerce.common.messaging.consumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.shino.ecommerce.common.messaging.dto.EmailDTO;
import com.shino.ecommerce.common.messaging.producer.EmailProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailProducer emailProducer;

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void consume(EmailDTO emailMessage) {
        emailProducer.sendEmailAsync(emailMessage);
    }
    
}
