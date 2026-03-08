package com.example.calculator_service.service;

import com.example.calculator_service.config.CreditProperties;
import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {
    
    private final CreditProperties creditProperties;
    private final PrescoringService prescoringService;
    
    public List<LoanOfferDto> generateOffers(LoanStatementRequestDto request) {
        log.info("Начало генерации предложений для запроса: {}", request);
        
        prescoringService.validateRequest(request);
        
        List<LoanOfferDto> offers = new ArrayList<>();
        
        boolean[] insuranceOptions = {false, true};
        boolean[] salaryOptions = {false, true};
        
        for (boolean insurance : insuranceOptions) {
            for (boolean salary : salaryOptions) {
                LoanOfferDto offer = createOffer(request, insurance, salary);
                offers.add(offer);
                log.debug("Создано предложение: insurance={}, salary={}, rate={}", 
                         insurance, salary, offer.getRate());
            }
        }
        
        offers.sort(Comparator.comparing(LoanOfferDto::getRate));
        log.info("Сгенерировано {} предложений", offers.size());
        
        return offers;
    }
    
    private LoanOfferDto createOffer(LoanStatementRequestDto request, 
                                     boolean isInsuranceEnabled, 
                                     boolean isSalaryClient) {
        
        BigDecimal rate = creditProperties.getBaseRate();
        BigDecimal totalAmount = request.getAmount();
        
        if (isInsuranceEnabled) {
            rate = rate.subtract(creditProperties.getInsurance().getDiscount());
            BigDecimal insuranceCost = calculateInsuranceCost(request.getAmount());
            totalAmount = totalAmount.add(insuranceCost);
        }
        
        if (isSalaryClient) {
            rate = rate.subtract(creditProperties.getSalary().getDiscount());
        }
        
        BigDecimal monthlyPayment = calculateMonthlyPayment(
            totalAmount, 
            rate, 
            request.getTerm()
        );
        
        return LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .requestedAmount(request.getAmount())
                .totalAmount(totalAmount)
                .term(request.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }
    
    private BigDecimal calculateInsuranceCost(BigDecimal amount) {
        BigDecimal millions = amount.divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP);
        return millions.multiply(creditProperties.getInsurance().getCostPerMillion());
    }
    
    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal annualRate, int term) {
        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal ratePower = onePlusRate.pow(term);
        
        BigDecimal numerator = monthlyRate.multiply(ratePower);
        BigDecimal denominator = ratePower.subtract(BigDecimal.ONE);
        
        BigDecimal annuityFactor = numerator.divide(denominator, 10, RoundingMode.HALF_UP);
        
        return amount.multiply(annuityFactor)
                .setScale(2, RoundingMode.HALF_UP);
    }
}