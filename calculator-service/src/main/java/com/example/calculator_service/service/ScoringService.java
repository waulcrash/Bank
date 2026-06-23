package com.example.calculator_service.service;

import com.example.calculator.dto.ScoringDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class ScoringService {
    
    public void validateScoringData(ScoringDataDto scoringData) {
        log.debug("Начало скоринга данных");
        
        validateAge(scoringData.getBirthdate());
        validatePassportData(scoringData);
        validateRequiredFields(scoringData);
        
        log.debug("Скоринг успешно пройден");
    }
    
    private void validateAge(LocalDate birthdate) {
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        
        if (age < 21) {
            throw new IllegalArgumentException("Клиент должен быть старше 21 года");
        }
        
        if (age > 65) {
            throw new IllegalArgumentException("Клиент должен быть младше 65 лет");
        }
    }
    
    private void validatePassportData(ScoringDataDto scoringData) {
        if (!scoringData.getPassportSeries().matches("\\d{4}")) {
            throw new IllegalArgumentException("Некорректная серия паспорта");
        }
        if (!scoringData.getPassportNumber().matches("\\d{6}")) {
            throw new IllegalArgumentException("Некорректный номер паспорта");
        }
    }
    
    private void validateRequiredFields(ScoringDataDto scoringData) {
        if (scoringData.getAmount() == null || scoringData.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Некорректная сумма кредита");
        }
        if (scoringData.getTerm() == null || scoringData.getTerm() <= 0) {
            throw new IllegalArgumentException("Некорректный срок кредита");
        }
    }
}