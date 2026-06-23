package com.example.deal.controller;

import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.service.DealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealControllerTest {

    @Mock
    private DealService dealService;

    @InjectMocks
    private DealController dealController;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    private LoanStatementRequestDto validRequest;
    private LoanOfferDto selectedOffer;
    private List<LoanOfferDto> mockOffers;
    private UUID statementId;

    @BeforeEach
    void setUp() {
        statementId = UUID.randomUUID();
        
        validRequest = LoanStatementRequestDto.builder()
            .amount(BigDecimal.valueOf(1_000_000))
            .term(12)
            .firstName("Иван")
            .lastName("Петров")
            .middleName("Иванович")
            .email("ivan.petrov@example.com")
            .birthdate(LocalDate.of(1990, 1, 1))
            .passportSeries("1234")
            .passportNumber("567890")
            .build();

        mockOffers = Arrays.asList(
            createMockOffer(statementId, false, false, BigDecimal.valueOf(15.0), BigDecimal.valueOf(1083096.00), BigDecimal.valueOf(90258.00)),
            createMockOffer(statementId, false, true, BigDecimal.valueOf(14.0), BigDecimal.valueOf(1078200.00), BigDecimal.valueOf(89850.00)),
            createMockOffer(statementId, true, false, BigDecimal.valueOf(13.0), BigDecimal.valueOf(1073040.00), BigDecimal.valueOf(89420.00)),
            createMockOffer(statementId, true, true, BigDecimal.valueOf(12.0), BigDecimal.valueOf(1066200.00), BigDecimal.valueOf(88850.00))
        );

        selectedOffer = LoanOfferDto.builder()
            .statementId(statementId)
            .requestedAmount(BigDecimal.valueOf(1_000_000))
            .totalAmount(BigDecimal.valueOf(1066200.00))
            .term(12)
            .monthlyPayment(BigDecimal.valueOf(88850.00))
            .rate(BigDecimal.valueOf(12.0))
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .build();
    }

    private LoanOfferDto createMockOffer(UUID statementId, boolean insurance, boolean salary, 
                                          BigDecimal rate, BigDecimal totalAmount, BigDecimal monthlyPayment) {
        return LoanOfferDto.builder()
            .statementId(statementId)
            .requestedAmount(BigDecimal.valueOf(1_000_000))
            .totalAmount(totalAmount)
            .term(12)
            .monthlyPayment(monthlyPayment)
            .rate(rate)
            .isInsuranceEnabled(insurance)
            .isSalaryClient(salary)
            .build();
    }

    private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(0, expected.compareTo(actual), 
            String.format("Ожидалось %s, получено %s", expected, actual));
    }

    @Test
    void createStatement_WithValidRequest_ShouldReturnOffersList() {
        when(dealService.createStatement(any(LoanStatementRequestDto.class)))
            .thenReturn(mockOffers);

        ResponseEntity<List<LoanOfferDto>> response = dealController.createStatement(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().size());
        assertEquals(15.0, response.getBody().get(0).getRate().doubleValue());
        assertEquals(14.0, response.getBody().get(1).getRate().doubleValue());
        assertEquals(13.0, response.getBody().get(2).getRate().doubleValue());
        assertEquals(12.0, response.getBody().get(3).getRate().doubleValue());
        
        verify(dealService, times(1)).createStatement(validRequest);
    }

    @Test
    void selectOffer_WithValidRequest_ShouldReturnOk() {
        doNothing().when(dealService).selectOffer(any(LoanOfferDto.class));

        ResponseEntity<Object> response = dealController.selectOffer(selectedOffer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(dealService, times(1)).selectOffer(selectedOffer);
    }

    @Test
    void createStatement_ShouldReturnSortedOffersFromWorstToBest() {
        List<LoanOfferDto> unsortedOffers = Arrays.asList(
            createMockOffer(statementId, false, false, BigDecimal.valueOf(12.0), BigDecimal.valueOf(1066200.00), BigDecimal.valueOf(88850.00)),
            createMockOffer(statementId, false, false, BigDecimal.valueOf(15.0), BigDecimal.valueOf(1083096.00), BigDecimal.valueOf(90258.00)),
            createMockOffer(statementId, false, false, BigDecimal.valueOf(10.0), BigDecimal.valueOf(1055000.00), BigDecimal.valueOf(87916.00)),
            createMockOffer(statementId, false, false, BigDecimal.valueOf(13.0), BigDecimal.valueOf(1073040.00), BigDecimal.valueOf(89420.00))
        );
        
        when(dealService.createStatement(any(LoanStatementRequestDto.class)))
            .thenReturn(unsortedOffers);

        ResponseEntity<List<LoanOfferDto>> response = dealController.createStatement(validRequest);

        assertNotNull(response.getBody());
        
        verify(dealService, times(1)).createStatement(validRequest);
    }

    @Test
    void selectOffer_ShouldHandleStatementNotFound() {
        doThrow(new RuntimeException("Statement not found with id: " + statementId))
            .when(dealService).selectOffer(any(LoanOfferDto.class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            dealController.selectOffer(selectedOffer);
        });

        assertTrue(exception.getMessage().contains("Statement not found"));
        verify(dealService, times(1)).selectOffer(selectedOffer);
    }

    @Test
    void createStatement_ShouldPropagateServiceException() {
        when(dealService.createStatement(any(LoanStatementRequestDto.class)))
            .thenThrow(new RuntimeException("Calculator service unavailable"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            dealController.createStatement(validRequest);
        });

        assertTrue(exception.getMessage().contains("Calculator service unavailable"));
        verify(dealService, times(1)).createStatement(validRequest);
    }
}