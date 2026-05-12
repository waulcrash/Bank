package com.example.dossier.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailSenderService {

    private static final Logger log = LoggerFactory.getLogger(MailSenderService.class);
    
    @Value("${spring.mail.username}")
    private String from;
    
    private final JavaMailSender mailSender;
    
    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendEmail(String to, String subject, String text) {
        log.info("Sending email to: {}, subject: {}", to, subject);
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(buildHtmlTemplate(text), true);
            helper.setFrom(from);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Error sending email", e);
        }
    }
    
    private String buildHtmlTemplate(String text) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"></head>
            <body style="font-family: Arial, sans-serif;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                    <h2 style="color: #5B35D5;">Ваш кредитный договор</h2>
                    <p>Уважаемый клиент!</p>
                    <p>%s</p>
                    <hr>
                    <p style="font-size: 12px; color: #999;">Это автоматическое сообщение, пожалуйста, не отвечайте на него.</p>
                </div>
            </body>
            </html>
            """.formatted(text);
    }
}