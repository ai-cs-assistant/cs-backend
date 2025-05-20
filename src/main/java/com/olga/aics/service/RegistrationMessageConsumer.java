package com.olga.aics.service;

import com.olga.aics.config.RabbitMQConfig;
import com.olga.aics.dto.RegistrationEmailMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationMessageConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleRegistrationEmail(RegistrationEmailMessage message) {
        try {
            log.info("開始處理註冊郵件發送: {}", message.getEmail());
            emailService.sendRegistrationEmail(message.getEmail(), message.getUsername());
            log.info("註冊郵件發送成功: {}", message.getEmail());
        } catch (MessagingException e) {
            log.error("註冊郵件發送失敗: {}", message.getEmail(), e);
            // 這裡可以添加重試邏輯或錯誤處理
        }
    }
} 