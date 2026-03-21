package com.example.deal.client;

import com.example.deal.dto.CreditDto;
import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.dto.ScoringDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Slf4j
@Component
public class CalculatorClient {
    
    private final WebClient webClient;
    
    public CalculatorClient(WebClient.Builder webClientBuilder,
                           @Value("${calculator.url}") String calculatorUrl) {
        this.webClient = webClientBuilder.baseUrl(calculatorUrl).build();
        log.info("CalculatorClient initialized with URL: {}", calculatorUrl);
    }
    
    public List<LoanOfferDto> getOffers(LoanStatementRequestDto request) {
        log.info("Calling calculator /offers endpoint");
        
        try {
            List<LoanOfferDto> offers = webClient.post()
                .uri("/calculator/offers")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(LoanOfferDto.class)
                .collectList()
                .block();
            
            log.info("Received {} offers from calculator", offers != null ? offers.size() : 0);
            return offers;
        } catch (Exception e) {
            log.error("Error calling calculator /offers", e);
            throw new RuntimeException("Failed to get offers from calculator", e);
        }
    }
    
    public CreditDto calculateCredit(ScoringDataDto scoringData) {
        log.info("Calling calculator /calc endpoint");
        
        try {
            CreditDto credit = webClient.post()
                .uri("/calculator/calc")
                .bodyValue(scoringData)
                .retrieve()
                .bodyToMono(CreditDto.class)
                .block();
            
            log.info("Received credit calculation result with amount: {}", 
                credit != null ? credit.getAmount() : null);
            return credit;
        } catch (Exception e) {
            log.error("Error calling calculator /calc", e);
            throw new RuntimeException("Failed to calculate credit", e);
        }
    }
}