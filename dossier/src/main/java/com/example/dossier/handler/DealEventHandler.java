package com.example.dossier.handler;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.study.dossier.dto.EmailMessage;
import neo.study.dossier.service.MailSenderService;

@Slf4j
@Component
@RequiredArgsConstructor
public class DealEventHandler {
    
    private final MailSenderService mailSenderService;
    
    @KafkaListener(topics = { "finish-registration", "send-documents", "send-ses", "credit-issued" })
    public void handleEmailMessage(EmailMessage emailMessage) {
        log.info("Received email message from Kafka: address={}, theme={}, statementId={}, text={}",
                emailMessage.getAddress(), 
                emailMessage.getTheme(), 
                emailMessage.getStatementId(), 
                emailMessage.getText());
        
        mailSenderService.sendEmail(
                emailMessage.getAddress(), 
                getTheme(emailMessage),
                emailMessage.getText()
        );
    }
    
    private String getTheme(EmailMessage emailMessage) {
        return Optional.ofNullable(emailMessage.getTheme())
                .map(String::valueOf)
                .orElse("Default Subject");
    }
}