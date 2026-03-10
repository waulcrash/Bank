package com.example.calculator_service.controller;

import com.example.calculator.dto.CreditDto;
import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import com.example.calculator.dto.ScoringDataDto;
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
    
    private LoanStatementRequestDto validLoanRequest;
    private ScoringDataDto validScoringData;
    private List<LoanOfferDto> mockOffers;
    private CreditDto mockCredit;
    
    @BeforeEach
    void setUp() {
        // Для /offers
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
        
        // Для /calc
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
        
        // Моковые предложения
        mockOffers = Arrays.asList(
            createMockOffer(false, false, BigDecimal.valueOf(15.0)),
            createMockOffer(false, true, BigDecimal.valueOf(14.0)),
            createMockOffer(true, false, BigDecimal.valueOf(12.0)),
            createMockOffer(true, true, BigDecimal.valueOf(11.0))
        );
        
        // Моковый кредит
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
    
    // Вспомогательный метод для сравнения BigDecimal
    private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(0, expected.compareTo(actual), 
            String.format("Ожидалось %s, получено %s", expected, actual));
    }
    
    // /calculator/offers 
 
    @Test
    void offers_ShouldReturnAllFourCombinations() {
        when(offerService.generateOffers(any(LoanStatementRequestDto.class)))
            .thenReturn(mockOffers);
        
        
        ResponseEntity<List<LoanOfferDto>> response = calculatorController.offers(validLoanRequest);
        
        List<LoanOfferDto> offers = response.getBody();
        
        boolean[][] combinations = {{false, false}, {false, true}, {true, false}, {true, true}};
        
        for (boolean[] comb : combinations) {
            boolean insurance = comb[0];
            boolean salary = comb[1];
            
            boolean found = offers.stream()
                .anyMatch(o -> o.getIsInsuranceEnabled() == insurance && 
                               o.getIsSalaryClient() == salary);
            
            assertTrue(found, 
                String.format("Комбинация insurance=%s, salary=%s не найдена", insurance, salary));
        }
    }
    
    @Test
    void offers_WhenServiceThrowsException_ShouldPropagateException() {
        when(offerService.generateOffers(any(LoanStatementRequestDto.class)))
            .thenThrow(new IllegalArgumentException("Ошибка валидации"));
        
        assertThrows(IllegalArgumentException.class, 
            () -> calculatorController.offers(validLoanRequest));
    }
    
    // /calculator/calc 
    
    @Test
    void calc_WithValidRequest_ShouldReturnCredit() {
        when(creditCalculationService.calculateCredit(any(ScoringDataDto.class)))
            .thenReturn(mockCredit);
        
        ResponseEntity<CreditDto> response = calculatorController.calc(validScoringData);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        CreditDto credit = response.getBody();
        assertBigDecimalEquals(BigDecimal.valueOf(1_000_000), credit.getAmount());
        assertEquals(12, credit.getTerm());
        assertBigDecimalEquals(BigDecimal.valueOf(90_258.00), credit.getMonthlyPayment());
        assertBigDecimalEquals(BigDecimal.valueOf(11.0), credit.getRate());
        assertBigDecimalEquals(BigDecimal.valueOf(16.2), credit.getPsk());
        assertTrue(credit.getIsInsuranceEnabled());
        assertTrue(credit.getIsSalaryClient());
        assertNotNull(credit.getPaymentSchedule());
        
        verify(creditCalculationService, times(1)).calculateCredit(validScoringData);
    }
    
    @Test
    void calc_WithDifferentOptions_ShouldReturnCorrectRate() {
        // Тест для разных комбинаций
        Object[][] testCases = {
            {false, false, 15.0},
            {false, true, 14.0},
            {true, false, 12.0},
            {true, true, 11.0}
        };
        
        for (Object[] testCase : testCases) {
            boolean insurance = (boolean) testCase[0];
            boolean salary = (boolean) testCase[1];
            double expectedRate = (double) testCase[2];
            
            // Мок с параметрами подходящими
            CreditDto mockCreditWithOptions = CreditDto.builder()
                    .amount(BigDecimal.valueOf(1_000_000))
                    .term(12)
                    .monthlyPayment(BigDecimal.valueOf(90_258.00))
                    .rate(BigDecimal.valueOf(expectedRate))
                    .psk(BigDecimal.valueOf(16.2))
                    .isInsuranceEnabled(insurance)
                    .isSalaryClient(salary)
                    .paymentSchedule(List.of())
                    .build();
            
            when(creditCalculationService.calculateCredit(any(ScoringDataDto.class)))
                .thenReturn(mockCreditWithOptions);
            
            // Запрос с нужными опциями
            ScoringDataDto requestWithOptions = ScoringDataDto.builder()
                    .amount(BigDecimal.valueOf(1_000_000))
                    .term(12)
                    .firstName("Иван")
                    .lastName("Иванов")
                    .birthdate(LocalDate.of(1990, 1, 1))
                    .passportSeries("1234")
                    .passportNumber("123456")
                    .isInsuranceEnabled(insurance)
                    .isSalaryClient(salary)
                    .build();
            
            ResponseEntity<CreditDto> response = calculatorController.calc(requestWithOptions);
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertBigDecimalEquals(BigDecimal.valueOf(expectedRate), response.getBody().getRate());
            assertEquals(insurance, response.getBody().getIsInsuranceEnabled());
            assertEquals(salary, response.getBody().getIsSalaryClient());
        }
    }
    
    @Test
    void calc_WhenServiceThrowsException_ShouldPropagateException() {
      
        when(creditCalculationService.calculateCredit(any(ScoringDataDto.class)))
            .thenThrow(new IllegalArgumentException("Ошибка скоринга"));
        
        assertThrows(IllegalArgumentException.class, 
            () -> calculatorController.calc(validScoringData));
    }
    
    // Проверка логов
    
    @Test
    void offers_ShouldLogRequestAndResponse() {
        // Просто проверяем, что метод работает без ошибок
        when(offerService.generateOffers(any())).thenReturn(mockOffers);
        
        ResponseEntity<List<LoanOfferDto>> response = calculatorController.offers(validLoanRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void calc_ShouldLogRequestAndResponse() {
        // Здесь так же проверяем на ошибки
        when(creditCalculationService.calculateCredit(any())).thenReturn(mockCredit);
        
        ResponseEntity<CreditDto> response = calculatorController.calc(validScoringData);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}