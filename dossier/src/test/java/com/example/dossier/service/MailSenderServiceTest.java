package com.example.dossier.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private MailSenderService mailSenderService;

    @Test
    void sendEmail_ShouldSendEmailSuccessfully() throws Exception {
        // given
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test message";
        String htmlContent = "<html>Test message</html>";

        // Устанавливаем from адрес через Reflection
        ReflectionTestUtils.setField(mailSenderService, "from", "test@example.com");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("email-template"), any(Context.class))).thenReturn(htmlContent);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // when
        mailSenderService.sendEmail(to, subject, text);

        // then
        verify(templateEngine, times(1)).process(eq("email-template"), any(Context.class));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}