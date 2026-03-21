package com.example.calculator_service.service;


import com.example.calculator.dto.ScoringDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {
    
    private ScoringService scoringService;
    private ScoringDataDto validData;
    
    @BeforeEach
    void setUp() {
        scoringService = new ScoringService();
        
        validData = ScoringDataDto.builder()
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
    }
    
    @Test
    void validateScoringData_WithValidData_ShouldPass() {
        assertDoesNotThrow(() -> scoringService.validateScoringData(validData));
    }
    
    @Test
    void validateScoringData_WhenAgeLessThan21_ShouldThrow() {
        validData.setBirthdate(LocalDate.now().minusYears(20));
        
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> scoringService.validateScoringData(validData));
        
        assertEquals("Клиент должен быть старше 21 года", ex.getMessage());
    }
    
    @Test
    void validateScoringData_WhenAgeMoreThan65_ShouldThrow() {
        validData.setBirthdate(LocalDate.now().minusYears(66));
        
        assertThrows(IllegalArgumentException.class,
            () -> scoringService.validateScoringData(validData));
    }
    
    @Test
    void validateScoringData_WhenPassportSeriesInvalid_ShouldThrow() {
        validData.setPassportSeries("123");
        
        assertThrows(IllegalArgumentException.class,
            () -> scoringService.validateScoringData(validData));
    }
    
    @Test
    void validateScoringData_WhenAmountIsNull_ShouldThrow() {
        validData.setAmount(null);
        
        assertThrows(IllegalArgumentException.class,
            () -> scoringService.validateScoringData(validData));
    }
}