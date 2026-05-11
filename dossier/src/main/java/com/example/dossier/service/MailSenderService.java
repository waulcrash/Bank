package com.example.dossier.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderService {
    
    @Value("${spring.mail.username}")
    private String from;
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    public void sendEmail(String to, String subject, String text) {
        log.info("Sending email to: {}, subject: {}", to, subject);
        
        var thymeleafContext = new Context();
        thymeleafContext.setVariable("text", text);
        thymeleafContext.setVariable("subject", subject);
        
        var htmlContent = templateEngine.process("email-template", thymeleafContext);
        
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom(from);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Error sending email to: {}", to, e);
            throw new RuntimeException("Error sending email", e);
        }
    }
}