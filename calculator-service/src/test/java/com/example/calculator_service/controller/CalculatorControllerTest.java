package com.example.calculator_service.controller;

import com.example.calculator.dto.CreditDto;
import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import com.example.calculator.dto.ScoringDataDto;
import com.example.calculator_service.advice.ControllerAdvice;
import com.example.calculator.dto.ErrorResponse;
import com.example.calculator_service.service.CreditCalculationService;
import com.example.calculator_service.service.OfferService;
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
class CalculatorControllerTest {
    
    @Mock
    private OfferService offerService;
    
    @Mock
    private CreditCalculationService creditCalculationService;
    
    @InjectMocks
    private CalculatorController calculatorController;
    
    private ControllerAdvice exceptionHandler = new ControllerAdvice();
    private ObjectMapper objectMapper = new ObjectMapper();
    
    private LoanStatementRequestDto validLoanRequest;
    private ScoringDataDto validScoringData;
    private List<LoanOfferDto> mockOffers;
    private CreditDto mockCredit;
    
    @BeforeEach
    void setUp() {
        validLoanRequest = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(1_000_000))
                .term(12)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .email("ivan@example.com")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        
        validScoringData = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(1_000_000))
                .term(12)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();
        
        mockOffers = Arrays.asList(
            createMockOffer(false, false, BigDecimal.valueOf(15.0)),
            createMockOffer(false, true, BigDecimal.valueOf(14.0)),
            createMockOffer(true, false, BigDecimal.valueOf(12.0)),
            createMockOffer(true, true, BigDecimal.valueOf(11.0))
        );
        
        mockCredit = CreditDto.builder()
                .amount(BigDecimal.valueOf(1_000_000))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(90_258.00))
                .rate(BigDecimal.valueOf(11.0))
                .psk(BigDecimal.valueOf(16.2))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .paymentSchedule(List.of())
                .build();
    }
    
    private LoanOfferDto createMockOffer(boolean insurance, boolean salary, BigDecimal rate) {
        return LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .requestedAmount(BigDecimal.valueOf(1_000_000))
                .totalAmount(insurance ? BigDecimal.valueOf(1_100_000) : BigDecimal.valueOf(1_000_000))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(90_258.00))
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
    void offers_WithValidRequest_ShouldReturnOffersList() {
        when(offerService.generateOffers(any(LoanStatementRequestDto.class)))
            .thenReturn(mockOffers);
        
        ResponseEntity<List<LoanOfferDto>> response = calculatorController.offers(validLoanRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().size());
        verify(offerService, times(1)).generateOffers(validLoanRequest);
    }
    
    @Test
    void calc_WithValidRequest_ShouldReturnCredit() {
        when(creditCalculationService.calculateCredit(any(ScoringDataDto.class)))
            .thenReturn(mockCredit);
        
        ResponseEntity<CreditDto> response = calculatorController.calc(validScoringData);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertBigDecimalEquals(BigDecimal.valueOf(11.0), response.getBody().getRate());
        verify(creditCalculationService, times(1)).calculateCredit(validScoringData);
    }
}