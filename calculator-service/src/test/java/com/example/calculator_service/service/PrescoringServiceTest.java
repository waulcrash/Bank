package com.example.calculator_service.service;

import com.example.calculator.dto.LoanStatementRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PrescoringServiceTest {
    
    private PrescoringService prescoringService;
    private LoanStatementRequestDto validRequest;
    
    @BeforeEach
    void setUp() {
        prescoringService = new PrescoringService();
        
        validRequest = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(12)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .email("ivan@example.com")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
    }
    
    @Test
    void validateRequest_WithValidData_ShouldPass() {
        assertDoesNotThrow(() -> prescoringService.validateRequest(validRequest));
    }
    
    //<10000 - ожидаем исключение
    @Test
    void validateRequest_WithInvalidAmount_ShouldThrow() {
        validRequest.setAmount(BigDecimal.valueOf(1000));
        assertThrows(IllegalArgumentException.class, 
            () -> prescoringService.validateRequest(validRequest));
    }
    
    @Test
    void validateRequest_WithInvalidTerm_ShouldThrow() {
        validRequest.setTerm(1);
        assertThrows(IllegalArgumentException.class, 
            () -> prescoringService.validateRequest(validRequest));
    }
    
    @Test
    void validateRequest_WithInvalidName_ShouldThrow() {
        validRequest.setFirstName("123");
        assertThrows(IllegalArgumentException.class, 
            () -> prescoringService.validateRequest(validRequest));
    }
    
    @Test
    void validateRequest_WithInvalidEmail_ShouldThrow() {
        validRequest.setEmail("invalid-email");
        assertThrows(IllegalArgumentException.class, 
            () -> prescoringService.validateRequest(validRequest));
    }
    
    @Test
    void validateRequest_WithUnderage_ShouldThrow() {
        validRequest.setBirthdate(LocalDate.now().minusYears(17));
        assertThrows(IllegalArgumentException.class, 
            () -> prescoringService.validateRequest(validRequest));
    }
    
    @Test
    void validateRequest_WithInvalidPassportSeries_ShouldThrow() {
        validRequest.setPassportSeries("123");
        assertThrows(IllegalArgumentException.class, 
            () -> prescoringService.validateRequest(validRequest));
    }
    
    @Test
    void validateRequest_WithInvalidPassportNumber_ShouldThrow() {
        validRequest.setPassportNumber("12345");
        assertThrows(IllegalArgumentException.class, 
            () -> prescoringService.validateRequest(validRequest));
    }
}