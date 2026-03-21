package com.example.calculator_service.controller;

import com.example.calculator.api.CalculatorControllerApi;
import com.example.calculator.dto.CreditDto;
import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import com.example.calculator.dto.ScoringDataDto;
import com.example.calculator_service.service.CreditCalculationService;
import com.example.calculator_service.service.OfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CalculatorController implements CalculatorControllerApi {

    private final OfferService offerService;
    private final CreditCalculationService creditCalculationService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> offers(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Получен запрос на расчет предложений: {}", loanStatementRequestDto);
        
        List<LoanOfferDto> offers = offerService.generateOffers(loanStatementRequestDto);
        
        log.info("Сгенерировано {} предложений", offers.size());
        return ResponseEntity.ok(offers);
    }

    @Override
    public ResponseEntity<CreditDto> calc(ScoringDataDto scoringDataDto) {
        log.info("Получен запрос на полный расчет кредита: {}", scoringDataDto);
        
        CreditDto credit = creditCalculationService.calculateCredit(scoringDataDto);
        
        log.info("Расчет кредита завершен: rate={}, monthlyPayment={}", 
                 credit.getRate(), credit.getMonthlyPayment());
        return ResponseEntity.ok(credit);
    }
}