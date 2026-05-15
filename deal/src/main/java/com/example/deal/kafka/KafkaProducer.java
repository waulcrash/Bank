package com.example.deal.kafka;

import com.example.deal.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, EmailMessage message) {
        log.info("Sending message to topic {}: address={}, statementId={}", 
            topic, message.getAddress(), message.getStatementId());
        kafkaTemplate.send(topic, message);
    }
}