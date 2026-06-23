package com.example.deal.service;

import com.example.deal.client.CalculatorClient;
import com.example.deal.dto.*;
import com.example.deal.entity.Client;
import com.example.deal.entity.Credit;
import com.example.deal.entity.Statement;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.CreditRepository;
import com.example.deal.repository.StatementRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private StatementRepository statementRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CalculatorClient calculatorClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DealService dealService;

    private LoanStatementRequestDto request;
    private Client client;
    private Statement statement;
    private List<LoanOfferDto> offers;
    private LoanOfferDto selectedOffer;
    private CreditDto creditDto;

    @BeforeEach
    void setUp() {
        request = LoanStatementRequestDto.builder()
            .amount(BigDecimal.valueOf(1000000))
            .term(12)
            .firstName("Иван")
            .lastName("Петров")
            .middleName("Иванович")
            .email("ivan@example.com")
            .birthdate(LocalDate.of(1990, 1, 1))
            .passportSeries("1234")
            .passportNumber("567890")
            .build();

        PassportDto passport = PassportDto.builder()
            .series("1234")
            .number("567890")
            .build();

        client = Client.builder()
            .id(UUID.randomUUID())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .middleName(request.getMiddleName())
            .email(request.getEmail())
            .birthDate(request.getBirthdate())
            .passport(passport)
            .build();

        
        List<StatementStatusHistoryDto> statusHistory = new ArrayList<>();
        statusHistory.add(StatementStatusHistoryDto.builder()
            .status(ApplicationStatus.PREAPPROVAL)
            .time(OffsetDateTime.now())
            .changeType(ChangeType.AUTOMATIC)
            .build());

        statement = Statement.builder()
            .id(UUID.randomUUID())
            .client(client)
            .status(ApplicationStatus.PREAPPROVAL)
            .creationDate(LocalDateTime.now())
            .statusHistory(statusHistory)
            .build();

        offers = List.of(
            LoanOfferDto.builder()
                .statementId(statement.getId())
                .requestedAmount(BigDecimal.valueOf(1000000))
                .rate(BigDecimal.valueOf(15.0))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(90258.00))
                .totalAmount(BigDecimal.valueOf(1083096.00))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build(),
            LoanOfferDto.builder()
                .statementId(statement.getId())
                .requestedAmount(BigDecimal.valueOf(1000000))
                .rate(BigDecimal.valueOf(12.0))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(88850.00))
                .totalAmount(BigDecimal.valueOf(1066200.00))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build()
        );

        selectedOffer = offers.get(1);

        creditDto = CreditDto.builder()
            .amount(BigDecimal.valueOf(1000000))
            .term(12)
            .monthlyPayment(BigDecimal.valueOf(88850.00))
            .rate(BigDecimal.valueOf(12.0))
            .psk(BigDecimal.valueOf(6.06))
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .paymentSchedule(List.of())
            .build();
    }

    @Test
    void createStatement_Success() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(statementRepository.save(any(Statement.class))).thenReturn(statement);
        when(calculatorClient.getOffers(any(LoanStatementRequestDto.class))).thenReturn(offers);

        List<LoanOfferDto> result = dealService.createStatement(request);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(statement.getId(), result.get(0).getStatementId());
        assertEquals(BigDecimal.valueOf(15.0), result.get(0).getRate());
        assertEquals(BigDecimal.valueOf(12.0), result.get(1).getRate());

        verify(clientRepository, times(1)).save(any(Client.class));
        verify(statementRepository, times(1)).save(any(Statement.class));
        verify(calculatorClient, times(1)).getOffers(any(LoanStatementRequestDto.class));
    }

    @Test
    void createStatement_OffersSortedFromWorstToBest() {
        List<LoanOfferDto> unsortedOffers = List.of(
            LoanOfferDto.builder().rate(BigDecimal.valueOf(10.0)).build(),
            LoanOfferDto.builder().rate(BigDecimal.valueOf(15.0)).build(),
            LoanOfferDto.builder().rate(BigDecimal.valueOf(12.0)).build(),
            LoanOfferDto.builder().rate(BigDecimal.valueOf(8.0)).build()
        );

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(statementRepository.save(any(Statement.class))).thenReturn(statement);
        when(calculatorClient.getOffers(any(LoanStatementRequestDto.class))).thenReturn(unsortedOffers);

        List<LoanOfferDto> result = dealService.createStatement(request);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.get(0).getRate().compareTo(result.get(1).getRate()) >= 0);
        assertTrue(result.get(1).getRate().compareTo(result.get(2).getRate()) >= 0);
        assertTrue(result.get(2).getRate().compareTo(result.get(3).getRate()) >= 0);
    }

    @Test
    void selectOffer_Success() throws JsonProcessingException {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        when(statementRepository.save(any(Statement.class))).thenReturn(statement);
        when(calculatorClient.calculateCredit(any(ScoringDataDto.class))).thenReturn(creditDto);
        when(creditRepository.save(any(Credit.class))).thenReturn(Credit.builder().id(UUID.randomUUID()).build());
        
        doReturn("{}").when(objectMapper).writeValueAsString(any(LoanOfferDto.class));
        doReturn("[]").when(objectMapper).writeValueAsString(any(List.class));

        assertDoesNotThrow(() -> dealService.selectOffer(selectedOffer));

        verify(statementRepository, times(1)).findById(any(UUID.class));
        verify(statementRepository, times(2)).save(any(Statement.class));
        verify(calculatorClient, times(1)).calculateCredit(any(ScoringDataDto.class));
        verify(creditRepository, times(1)).save(any(Credit.class));
        
        assertEquals(ApplicationStatus.CC_APPROVED, statement.getStatus());
        assertEquals(3, statement.getStatusHistory().size());
    }

    @Test
    void selectOffer_StatementNotFound_ThrowsException() {
        UUID statementId = UUID.randomUUID();
        selectedOffer.setStatementId(statementId);
        
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> dealService.selectOffer(selectedOffer));

        assertTrue(exception.getMessage().contains("Statement not found"));
        verify(statementRepository, never()).save(any(Statement.class));
        verify(calculatorClient, never()).calculateCredit(any(ScoringDataDto.class));
        verify(creditRepository, never()).save(any(Credit.class));
    }

    @Test
    void createStatement_ClientSavedWithPassport() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(statementRepository.save(any(Statement.class))).thenReturn(statement);
        when(calculatorClient.getOffers(any(LoanStatementRequestDto.class))).thenReturn(offers);

        dealService.createStatement(request);

        verify(clientRepository).save(argThat(savedClient -> 
            savedClient.getPassport() != null &&
            savedClient.getPassport().getSeries().equals("1234") &&
            savedClient.getPassport().getNumber().equals("567890")
        ));
    }

    @Test
    void selectOffer_StatusHistoryContainsAllSteps() throws JsonProcessingException {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        when(statementRepository.save(any(Statement.class))).thenReturn(statement);
        when(calculatorClient.calculateCredit(any(ScoringDataDto.class))).thenReturn(creditDto);
        when(creditRepository.save(any(Credit.class))).thenReturn(Credit.builder().id(UUID.randomUUID()).build());
        doReturn("{}").when(objectMapper).writeValueAsString(any(LoanOfferDto.class));
        doReturn("[]").when(objectMapper).writeValueAsString(any(List.class));

        dealService.selectOffer(selectedOffer);

        List<StatementStatusHistoryDto> history = statement.getStatusHistory();
        assertEquals(3, history.size());
        assertEquals(ApplicationStatus.PREAPPROVAL, history.get(0).getStatus());
        assertEquals(ChangeType.AUTOMATIC, history.get(0).getChangeType());
        assertEquals(ApplicationStatus.APPROVED, history.get(1).getStatus());
        assertEquals(ChangeType.MANUAL, history.get(1).getChangeType());
        assertEquals(ApplicationStatus.CC_APPROVED, history.get(2).getStatus());
        assertEquals(ChangeType.AUTOMATIC, history.get(2).getChangeType());
    }

    @Test
    void selectOffer_WhenSerializationFails_ThrowsException() throws JsonProcessingException {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        doThrow(new JsonProcessingException("Serialization error") {})
            .when(objectMapper).writeValueAsString(any(LoanOfferDto.class));

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> dealService.selectOffer(selectedOffer));

        assertTrue(exception.getMessage().contains("Error serializing applied offer"));
        verify(statementRepository, times(1)).findById(any(UUID.class));
        verify(statementRepository, never()).save(any(Statement.class));
        verify(calculatorClient, never()).calculateCredit(any(ScoringDataDto.class));
    }
}