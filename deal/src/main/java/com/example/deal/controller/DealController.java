package com.example.deal.controller;

import com.example.deal.api.DealControllerApi;
import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DealController implements DealControllerApi {
    
    private final DealService dealService;
    
    @Override
    public ResponseEntity<List<LoanOfferDto>> createStatement(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("POST /deal/statement - Request received");
        List<LoanOfferDto> offers = dealService.createStatement(loanStatementRequestDto);
        log.info("POST /deal/statement - Returning {} offers", offers.size());
        return ResponseEntity.ok(offers);
    }
    
    @Override
    public ResponseEntity<Object> selectOffer(LoanOfferDto loanOfferDto) {
        log.info("POST /deal/offer/select - Request for statement: {}", loanOfferDto.getStatementId());
        dealService.selectOffer(loanOfferDto);
        
        log.info("POST /deal/offer/select - Success");
        return ResponseEntity.ok().build();
    }
}