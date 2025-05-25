package com.shino.ecommerce.features.auth.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.shino.ecommerce.features.auth.dto.EmailMessageDTO;

import java.util.Properties;

@Service
public class EmailService {
    
    @Value("${spring.mail.host}")
    private String mailHost;
    
    @Value("${spring.mail.port}")
    private int mailPort;
    
    @Value("${spring.mail.username}")
    private String mailUsername;
    
    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    private final RabbitTemplate rabbitTemplate;

    public EmailService(RabbitTemplate rabbitTemplate) {
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
        JavaMailSender mailSender = getJavaMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendVerificationEmail(String to, String verificationCode) {
        String subject = "Email Verification";
        String text = "Your verification code is: " + verificationCode;
        sendEmail(to, subject, text);
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Password Reset Request";
        String text = "Your password reset token is: " + resetToken;
        sendEmail(to, subject, text);
    }

    public void sendPasswordResetOTP(String to, String otp) {
        String subject = "Password Reset OTP";
        String text = "Your OTP for password reset is: " + otp + "\nThis OTP will expire in 5 minutes.";
        sendEmail(to, subject, text);
    }

    public void sendEmailAsync(String to, String subject, String text) {
        EmailMessageDTO emailMessage = new EmailMessageDTO(to, subject, text);
        rabbitTemplate.convertAndSend(exchange, routingkey, emailMessage);
    }

    public void sendVerificationEmailAsync(String to, String verificationCode) {
        String subject = "Email Verification";
        String text = "Your verification code is: " + verificationCode;
        sendEmailAsync(to, subject, text);
    }

    public void sendPasswordResetEmailAsync(String to, String resetToken) {
        String subject = "Password Reset Request";
        String text = "Your password reset token is: " + resetToken;
        sendEmailAsync(to, subject, text);
    }

    public void sendPasswordResetOTPAsync(String to, String otp) {
        String subject = "Password Reset OTP";
        String text = "Your OTP for password reset is: " + otp + "\nThis OTP will expire in 5 minutes.";
        sendEmailAsync(to, subject, text);
    }
}