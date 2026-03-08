package com.example.calculator_service.service;

import com.example.calculator.dto.LoanStatementRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class PrescoringService {
    
    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(10000);
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(10_000_000);
    private static final int MIN_TERM = 6;
    private static final int MAX_TERM = 60;
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 70;
    
    public void validateRequest(LoanStatementRequestDto request) {
        log.debug("Начало прескоринга для запроса");
        
        validateAmount(request.getAmount());
        validateTerm(request.getTerm());
        validateName(request.getFirstName(), "Имя");
        validateName(request.getLastName(), "Фамилия");
        validateEmail(request.getEmail());
        validateBirthdate(request.getBirthdate());
        validatePassport(request.getPassportSeries(), request.getPassportNumber());
        
        log.debug("Прескоринг успешно завершен");
    }
    
    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Сумма кредита не может быть null");
        }
        if (amount.compareTo(MIN_AMOUNT) < 0 || amount.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException(
                String.format("Сумма кредита должна быть от %s до %s", MIN_AMOUNT, MAX_AMOUNT)
            );
        }
    }
    
    private void validateTerm(Integer term) {
        if (term == null) {
            throw new IllegalArgumentException("Срок кредита не может быть null");
        }
        if (term < MIN_TERM || term > MAX_TERM) {
            throw new IllegalArgumentException(
                String.format("Срок кредита должен быть от %d до %d месяцев", MIN_TERM, MAX_TERM)
            );
        }
    }
    
    private void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
        if (!name.matches("^[a-zA-Zа-яА-Я]+$")) {
            throw new IllegalArgumentException(fieldName + " должно содержать только буквы");
        }
        if (name.length() < 2 || name.length() > 30) {
            throw new IllegalArgumentException(fieldName + " должно содержать от 2 до 30 символов");
        }
    }
    
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Некорректный формат email");
        }
    }
    
    private void validateBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("Дата рождения не может быть null");
        }
        
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException(
                String.format("Возраст должен быть от %d до %d лет", MIN_AGE, MAX_AGE)
            );
        }
    }
    
    private void validatePassport(String series, String number) {
        if (series == null || !series.matches("\\d{4}")) {
            throw new IllegalArgumentException("Серия паспорта должна содержать 4 цифры");
        }
        if (number == null || !number.matches("\\d{6}")) {
            throw new IllegalArgumentException("Номер паспорта должен содержать 6 цифр");
        }
    }
}