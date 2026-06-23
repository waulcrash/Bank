package com.example.dossier.handler;

import com.example.deal.dto.EmailMessage;
import com.example.dossier.service.MailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DealEventHandler {

    private static final Logger log = LoggerFactory.getLogger(DealEventHandler.class);
    private final MailSenderService mailSenderService;

    public DealEventHandler(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @KafkaListener(topics = "send-documents")
    public void handleEmailMessage(EmailMessage emailMessage) {
        log.info("Received email message: address={}, theme={}, statementId={}, text={}",
                emailMessage.getAddress(), emailMessage.getTheme(),
                emailMessage.getStatementId(), emailMessage.getText());

        mailSenderService.sendEmail(
                emailMessage.getAddress(),
                emailMessage.getTheme(),
                emailMessage.getText()
        );
    }
}