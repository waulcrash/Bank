package com.example.calculator_service.service;

import com.example.calculator_service.config.CreditProperties;
import com.example.calculator.dto.CreditDto;
import com.example.calculator.dto.PaymentScheduleElementDto;
import com.example.calculator.dto.ScoringDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCalculationServiceTest {
    
    @Mock
    private CreditProperties creditProperties;
    
    @Mock
    private ScoringService scoringService;
    
    @InjectMocks
    private CreditCalculationService creditCalculationService;
    
    private ScoringDataDto scoringData;
    
    @BeforeEach
    void setUp() {
        scoringData = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(1_000_000))
                .term(12)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
        
        // Настройка моков
        CreditProperties.Insurance insurance = new CreditProperties.Insurance();
        insurance.setDiscount(BigDecimal.valueOf(3.0));
        insurance.setCostPerMillion(BigDecimal.valueOf(100_000));
        
        CreditProperties.Salary salary = new CreditProperties.Salary();
        salary.setDiscount(BigDecimal.valueOf(1.0));
        
        lenient().when(creditProperties.getBaseRate()).thenReturn(BigDecimal.valueOf(15.0));
        lenient().when(creditProperties.getInsurance()).thenReturn(insurance);
        lenient().when(creditProperties.getSalary()).thenReturn(salary);
        
        doNothing().when(scoringService).validateScoringData(any());
    }
    
    @Test
    void calculateCredit_ShouldReturnCreditDto() {
        CreditDto result = creditCalculationService.calculateCredit(scoringData);
        
        assertNotNull(result);
        assertEquals(scoringData.getAmount(), result.getAmount());
        assertEquals(scoringData.getTerm(), result.getTerm());
        verify(scoringService, times(1)).validateScoringData(scoringData);
    }
    
    @Test
    void calculateCredit_WithNoOptions_ShouldUseBaseRate() {
        CreditDto result = creditCalculationService.calculateCredit(scoringData);
        
        assertEquals(0, BigDecimal.valueOf(15.0).compareTo(result.getRate()));
    }
    
    @Test
    void calculateCredit_WithInsuranceOnly_ShouldApplyDiscount() {
        scoringData.setIsInsuranceEnabled(true);
        
        CreditDto result = creditCalculationService.calculateCredit(scoringData);
        
        assertEquals(0, BigDecimal.valueOf(12.0).compareTo(result.getRate()));
    }
    
    @Test
    void calculateCredit_WithSalaryOnly_ShouldApplyDiscount() {
        scoringData.setIsSalaryClient(true);
        
        CreditDto result = creditCalculationService.calculateCredit(scoringData);
        
        assertEquals(0, BigDecimal.valueOf(14.0).compareTo(result.getRate()));
    }
    
    @Test
    void calculateCredit_WithBothOptions_ShouldApplyBothDiscounts() {
        scoringData.setIsInsuranceEnabled(true);
        scoringData.setIsSalaryClient(true);
        
        CreditDto result = creditCalculationService.calculateCredit(scoringData);
        
        assertEquals(0, BigDecimal.valueOf(11.0).compareTo(result.getRate()));
    }
    
    @Test
    void calculateCredit_ShouldGeneratePaymentSchedule() {
        scoringData.setTerm(12);
        
        CreditDto result = creditCalculationService.calculateCredit(scoringData);
        
        assertEquals(12, result.getPaymentSchedule().size());
        
        PaymentScheduleElementDto lastPayment = result.getPaymentSchedule().get(11);
        assertEquals(0, lastPayment.getRemainingDebt().compareTo(BigDecimal.ZERO));
    }
    
    @Test
    void calculateCredit_ShouldCalculatePsk() {
        CreditDto result = creditCalculationService.calculateCredit(scoringData);
        
        assertNotNull(result.getPsk());
        assertTrue(result.getPsk().compareTo(BigDecimal.ZERO) > 0);
    }
}