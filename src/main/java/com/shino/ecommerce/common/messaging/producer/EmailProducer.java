package com.shino.ecommerce.common.messaging.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.shino.ecommerce.common.messaging.dto.EmailDTO;

import java.util.Properties;

@Service
public class EmailProducer {
    
    @Value("${spring.mail.host}")
    private String mailHost;
    
    @Value("${spring.mail.port}")
    private int mailPort;
    
    @Value("${spring.mail.username}")
    private String mailUsername;
    
    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${rabbitmq.exchange.email}")
    private String exchange;

    @Value("${rabbitmq.routingkey.email}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        if (to == null || to.trim().isEmpty() || !to.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid recipient email address: " + to);
        }
        JavaMailSender mailSender = getJavaMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to.trim());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }    

    public void sendEmailAsync(EmailDTO emailMessage) {
        rabbitTemplate.convertAndSend(exchange, routingKey, emailMessage);
    }
    
}    
