package com.olga.aics.service;

import com.olga.aics.config.RabbitMQConfig;
import com.olga.aics.dto.RegistrationEmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendRegistrationEmail(RegistrationEmailMessage message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY,
            message
        );
    }
} 