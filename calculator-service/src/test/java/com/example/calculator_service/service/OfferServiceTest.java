package com.example.calculator_service.service;

import com.example.calculator_service.config.CreditProperties;
import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {
    
    @Mock
    private CreditProperties creditProperties;
    
    @Mock
    private PrescoringService prescoringService;
    
    @InjectMocks
    private OfferService offerService;
    
    private LoanStatementRequestDto request;
    
    @BeforeEach
    void setUp() {
        request = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(1_000_000))
                .term(12)
                .firstName("Иван")
                .lastName("Иванов")
                .email("ivan@example.com")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
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
        
        doNothing().when(prescoringService).validateRequest(any());
    }
    
    @Test
    void generateOffers_ShouldReturnFourOffers() {
        List<LoanOfferDto> offers = offerService.generateOffers(request);
        
        assertEquals(4, offers.size());
        verify(prescoringService, times(1)).validateRequest(request);
    }
    
    @Test
    void generateOffers_ShouldBeSortedByRate() {
        List<LoanOfferDto> offers = offerService.generateOffers(request);
        
        for (int i = 0; i < offers.size() - 1; i++) {
            assertTrue(offers.get(i).getRate().compareTo(offers.get(i + 1).getRate()) <= 0);
        }
    }
    
    @Test
    void generateOffers_WithInsuranceEnabled_ShouldAddCost() {
        List<LoanOfferDto> offers = offerService.generateOffers(request);
        
        LoanOfferDto insuranceOffer = offers.stream()
            .filter(o -> o.getIsInsuranceEnabled() && !o.getIsSalaryClient())
            .findFirst()
            .orElseThrow();
        
            assertEquals(0, BigDecimal.valueOf(1_100_000).compareTo(insuranceOffer.getTotalAmount()));
            assertEquals(0, BigDecimal.valueOf(12.0).compareTo(insuranceOffer.getRate()));
    }
    
    @Test
    void generateOffers_WithSalaryEnabled_ShouldReduceRate() {
        List<LoanOfferDto> offers = offerService.generateOffers(request);
        
        LoanOfferDto salaryOffer = offers.stream()
            .filter(o -> !o.getIsInsuranceEnabled() && o.getIsSalaryClient())
            .findFirst()
            .orElseThrow();
        
        assertEquals(BigDecimal.valueOf(14.0), salaryOffer.getRate());
    }
}