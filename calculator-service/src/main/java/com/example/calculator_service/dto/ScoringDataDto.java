package com.example.calculator_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoringDataDto {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private boolean isInsuranceEnabled;
    private boolean isSalaryClient;
}
