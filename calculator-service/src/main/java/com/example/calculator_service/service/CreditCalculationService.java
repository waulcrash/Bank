package com.example.calculator_service.service;

import com.example.calculator_service.config.CreditProperties;
import com.example.calculator.dto.CreditDto;
import com.example.calculator.dto.PaymentScheduleElementDto;
import com.example.calculator.dto.ScoringDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCalculationService {
    
    private final CreditProperties creditProperties;
    private final ScoringService scoringService;
    
    public CreditDto calculateCredit(ScoringDataDto scoringData) {
        log.info("Начало расчета кредита для данных: {}", scoringData);
        
        scoringService.validateScoringData(scoringData);
        
        BigDecimal rate = calculateFinalRate(scoringData);
        BigDecimal totalAmount = calculateTotalAmount(scoringData);
        BigDecimal monthlyPayment = calculateMonthlyPayment(
            totalAmount, 
            rate, 
            scoringData.getTerm()
        );
        
        List<PaymentScheduleElementDto> paymentSchedule = createPaymentSchedule(
            totalAmount,
            rate,
            scoringData.getTerm(),
            monthlyPayment
        );
        
        BigDecimal psk = calculatePsk(totalAmount, monthlyPayment, scoringData.getTerm());
        
        return CreditDto.builder()
                .amount(totalAmount)
                .term(scoringData.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                .isSalaryClient(scoringData.getIsSalaryClient())
                .paymentSchedule(paymentSchedule)
                .build();
    }
    
    private BigDecimal calculateFinalRate(ScoringDataDto scoringData) {
        BigDecimal rate = creditProperties.getBaseRate();
        
        if (scoringData.getIsInsuranceEnabled()) {
            rate = rate.subtract(creditProperties.getInsurance().getDiscount());
        }
        
        if (scoringData.getIsSalaryClient()) {
            rate = rate.subtract(creditProperties.getSalary().getDiscount());
        }
        
        return rate;
    }
    
    private BigDecimal calculateTotalAmount(ScoringDataDto scoringData) {
        BigDecimal amount = scoringData.getAmount();
        
        if (scoringData.getIsInsuranceEnabled()) {
            BigDecimal insuranceCost = calculateInsuranceCost(amount);
            amount = amount.add(insuranceCost);
        }
        
        return amount;
    }
    
    private BigDecimal calculateInsuranceCost(BigDecimal amount) {
        BigDecimal millions = amount.divide(BigDecimal.valueOf(1_000_000), 4, RoundingMode.HALF_UP);
        return millions.multiply(creditProperties.getInsurance().getCostPerMillion());
    }
    
    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal annualRate, int term) {
        // /12 /100
        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.CEILING)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.CEILING);
        
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal ratePower = onePlusRate.pow(term);
        
        BigDecimal numerator = monthlyRate.multiply(ratePower);
        BigDecimal denominator = ratePower.subtract(BigDecimal.ONE);
        
        BigDecimal annuityFactor = numerator.divide(denominator, 10, RoundingMode.CEILING);
        
        return amount.multiply(annuityFactor)
                .setScale(2, RoundingMode.CEILING);
    }
    
    private List<PaymentScheduleElementDto> createPaymentSchedule(
            BigDecimal amount, 
            BigDecimal annualRate, 
            int term,
            BigDecimal monthlyPayment) {
        
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        BigDecimal remainingDebt = amount;
        LocalDate paymentDate = LocalDate.now().plusMonths(1);
        
        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.CEILING)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.CEILING);
        
        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt
                    .multiply(monthlyRate)
                    .setScale(2, RoundingMode.CEILING);
            
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);
            
            if (i == term) {
                debtPayment = debtPayment.add(remainingDebt);
                remainingDebt = BigDecimal.ZERO;
                monthlyPayment = interestPayment.add(debtPayment);
            }
            
            PaymentScheduleElementDto element = PaymentScheduleElementDto.builder()
                    .number(i)
                    .date(paymentDate)
                    .totalPayment(monthlyPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(remainingDebt)
                    .build();
            
            schedule.add(element);
            paymentDate = paymentDate.plusMonths(1);
        }
        
        return schedule;
    }
    
    private BigDecimal calculatePsk(BigDecimal amount, BigDecimal monthlyPayment, int term) {
        BigDecimal totalPayments = monthlyPayment.multiply(BigDecimal.valueOf(term));
        BigDecimal overpayment = totalPayments.subtract(amount);
        
        return overpayment
                .divide(amount, 4, RoundingMode.CEILING)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.CEILING);
    }
}