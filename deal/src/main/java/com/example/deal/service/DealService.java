package com.example.deal.service;

import com.example.deal.client.CalculatorClient;
import com.example.deal.dto.*;
import com.example.deal.entity.Client;
import com.example.deal.entity.Credit;
import com.example.deal.entity.Statement;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.CreditRepository;
import com.example.deal.repository.StatementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {
    
    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;
    private final CalculatorClient calculatorClient;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public List<LoanOfferDto> createStatement(LoanStatementRequestDto request) {
        log.info("Creating statement for request: {}", request);
        
        // 1. Create passport DTO (сгенерированный)
        PassportDto passport = PassportDto.builder()
            .series(request.getPassportSeries())
            .number(request.getPassportNumber())
            .build();
        
        // 2. Create and save client with passport as JSONB
        Client client = Client.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .middleName(request.getMiddleName())
            .email(request.getEmail())
            .birthDate(request.getBirthdate())
            .passport(passport)
            .build();
        client = clientRepository.save(client);
        log.info("Client saved with id: {}", client.getId());
        
        // 3. Create statement with PREAPPROVAL status
        StatementStatusHistoryDto statusHistory = StatementStatusHistoryDto.builder()
            .status(ApplicationStatus.PREAPPROVAL)
            .time(OffsetDateTime.now())
            .changeType(ChangeType.AUTOMATIC)
            .build();
        
        Statement newStatement = Statement.builder()
            .client(client)
            .status(ApplicationStatus.PREAPPROVAL)
            .creationDate(LocalDateTime.now())
            .statusHistory(List.of(statusHistory))
            .build();
        
        Statement savedStatement = statementRepository.save(newStatement);
        log.info("Statement saved with id: {}", savedStatement.getId());
        
        // 4. Get offers from calculator
        List<LoanOfferDto> offers = calculatorClient.getOffers(request);
        
        // 5. Set statementId and sort from worst to best
        List<LoanOfferDto> sortedOffers = offers.stream()
            .peek(offer -> offer.setStatementId(savedStatement.getId()))
            .sorted(Comparator.comparing(LoanOfferDto::getRate).reversed())
            .collect(Collectors.toList());
        
        log.info("Returning {} offers sorted from worst to best", sortedOffers.size());
        return sortedOffers;
    }
    
    @Transactional
    public void selectOffer(LoanOfferDto selectedOffer) {
        log.info("Selecting offer for statement id: {}", selectedOffer.getStatementId());
        
        // 1. Find statement
        Statement statement = statementRepository.findById(selectedOffer.getStatementId())
            .orElseThrow(() -> new RuntimeException("Statement not found with id: " + selectedOffer.getStatementId()));
        
        // 2. Update status to APPROVED
        statement.setStatus(ApplicationStatus.APPROVED);
        
        // 3. Add to status history
        List<StatementStatusHistoryDto> statusHistory = statement.getStatusHistory();
        if (statusHistory == null) {
            statusHistory = new java.util.ArrayList<>();
            statement.setStatusHistory(statusHistory);
        }
        
        statusHistory.add(StatementStatusHistoryDto.builder()
            .status(ApplicationStatus.APPROVED)
            .time(OffsetDateTime.now())
            .changeType(ChangeType.MANUAL)
            .build());
        
        // 4. Save applied offer
        try {
            String appliedOfferJson = objectMapper.writeValueAsString(selectedOffer);
            statement.setAppliedOffer(appliedOfferJson);
        } catch (Exception e) {
            log.error("Error serializing applied offer", e);
            throw new RuntimeException("Error serializing applied offer", e);
        }
        
        statementRepository.save(statement);
        log.info("Statement approved");
        
        // 5. Prepare scoring data - используем passport из client
        ScoringDataDto scoringData = ScoringDataDto.builder()
            .amount(selectedOffer.getRequestedAmount())
            .term(selectedOffer.getTerm())
            .firstName(statement.getClient().getFirstName())
            .lastName(statement.getClient().getLastName())
            .middleName(statement.getClient().getMiddleName())
            .birthdate(statement.getClient().getBirthDate())
            .passportSeries(statement.getClient().getPassport().getSeries())
            .passportNumber(statement.getClient().getPassport().getNumber())
            .isInsuranceEnabled(selectedOffer.getIsInsuranceEnabled())
            .isSalaryClient(selectedOffer.getIsSalaryClient())
            .build();
        
        // 6. Calculate credit
        CreditDto creditDto = calculatorClient.calculateCredit(scoringData);
        
        // 7. Create and save credit
        Credit credit = Credit.builder()
            .amount(creditDto.getAmount())
            .term(creditDto.getTerm())
            .monthlyPayment(creditDto.getMonthlyPayment())
            .rate(creditDto.getRate())
            .insuranceEnabled(creditDto.getIsInsuranceEnabled())
            .salaryClient(creditDto.getIsSalaryClient())
            .build();
        
        try {
            String paymentScheduleJson = objectMapper.writeValueAsString(creditDto.getPaymentSchedule());
            credit.setPaymentSchedule(paymentScheduleJson);
        } catch (Exception e) {
            log.error("Error serializing payment schedule", e);
            throw new RuntimeException("Error serializing payment schedule", e);
        }
        
        Credit savedCredit = creditRepository.save(credit);
        log.info("Credit saved with id: {}", savedCredit.getId());
        
        // 8. Update statement with credit and CC_APPROVED status
        statement.setCredit(savedCredit);
        statement.setStatus(ApplicationStatus.CC_APPROVED);
        
        statusHistory.add(StatementStatusHistoryDto.builder()
            .status(ApplicationStatus.CC_APPROVED)
            .time(OffsetDateTime.now())
            .changeType(ChangeType.AUTOMATIC)
            .build());
        
        statementRepository.save(statement);
        log.info("Statement updated with credit and CC_APPROVED status");
    }
}