package com.example.dossier.handler;

import com.example.deal.dto.EmailMessage;
import com.example.dossier.service.MailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealEventHandlerTest {

    @Mock
    private MailSenderService mailSenderService;

    @InjectMocks
    private DealEventHandler dealEventHandler;

    @Test
    void handleEmailMessage_ShouldCallMailSenderService() {
        // given
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setAddress("test@example.com");
        emailMessage.setTheme("DOCUMENTS_CREATED");
        emailMessage.setStatementId(12345L);
        emailMessage.setText("Test message");

        // when
        dealEventHandler.handleEmailMessage(emailMessage);

        // then
        verify(mailSenderService, times(1)).sendEmail(
            emailMessage.getAddress(),
            emailMessage.getTheme(),
            emailMessage.getText()
        );
    }
}