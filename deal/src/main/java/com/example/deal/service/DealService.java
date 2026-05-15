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
import com.example.deal.kafka.KafkaProducer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.deal.kafka.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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
    

    private final KafkaTemplate<String, Object> kafkaTemplate;
    
/**
     * Создание кредитной заявки и получение списка предложений
     *
     * 1. Создание объекта паспорта из входных данных
     * 2. Сохранение клиента в БД
     * 3. Создание заявки со статусом PREAPPROVAL
     * 4. Запрос предложений от калькулятора
     * 5. Сортировка предложений от худшего к лучшему
     * 6. Возврат списка предложений клиентe
     */

    @Transactional
    public List<LoanOfferDto> createStatement(LoanStatementRequestDto request) {
        log.info("Creating statement for request: {}", request);
        
        // 1. Паспортные данные как jsonb
        PassportDto passport = PassportDto.builder()
            .series(request.getPassportSeries())
            .number(request.getPassportNumber())
            .build();
        
        // 2. Сущность клиента
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
        
        // 3. Запись в истории статусов
        List<StatementStatusHistoryDto> statusHistory = new ArrayList<>();
        statusHistory.add(StatementStatusHistoryDto.builder()
            .status(ApplicationStatus.PREAPPROVAL)
            .time(OffsetDateTime.now())
            .changeType(ChangeType.AUTOMATIC)
            .build());
        
        // 4. Заявка
        Statement newStatement = Statement.builder()
            .client(client)
            .status(ApplicationStatus.PREAPPROVAL)
            .creationDate(LocalDateTime.now())
            .statusHistory(statusHistory)  
            .build();
        
        Statement savedStatement = statementRepository.save(newStatement);
        log.info("Statement saved with id: {}", savedStatement.getId());
        
        // 4. Расчет для 4 вариантов
        List<LoanOfferDto> offers = calculatorClient.getOffers(request);
        
        // 5. Обработка и сортировка
        List<LoanOfferDto> sortedOffers = offers.stream()
            .peek(offer -> offer.setStatementId(savedStatement.getId()))
            .sorted(Comparator.comparing(LoanOfferDto::getRate).reversed())
            .collect(Collectors.toList());
        
        log.info("Returning {} offers sorted from worst to best", sortedOffers.size());
        return sortedOffers;
    }
    
 /**
     * Выбор кредитного предложения клиентом
     *
     * 1. Поиск заявки по ID
     * 2. Обновление статуса заявки на APPROVED
     * 3. Сохранение выбранного предложения в JSONB-поле
     * 4. Подготовка данных для скоринга
     * 5. Расчет кредита через калькулятор
     * 6. Сохранение кредита в БД
     * 7. Обновление статуса заявки на CC_APPROVED
     */ 

    @Transactional
    public void selectOffer(LoanOfferDto selectedOffer) {
        log.info("Selecting offer for statement id: {}", selectedOffer.getStatementId());
        
        // 1. Поиск заявки по ID
        Statement statement = statementRepository.findById(selectedOffer.getStatementId())
            .orElseThrow(() -> new RuntimeException("Statement not found with id: " + selectedOffer.getStatementId()));
        
        // 2. Обновление статуса заявки на APPROVED
        statement.setStatus(ApplicationStatus.APPROVED);
        
        // 3. Запись в историю статусов
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
        
        // 4. Серилизация выбранного предложения json и сохранение
        try {
            String appliedOfferJson = objectMapper.writeValueAsString(selectedOffer);
            statement.setAppliedOffer(appliedOfferJson);
        } catch (Exception e) {
            log.error("Error serializing applied offer", e);
            throw new RuntimeException("Error serializing applied offer", e);
        }
        
        statementRepository.save(statement);
        log.info("Statement approved");
        
        // 5. Сборка из заявки и предложения
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
        
        // 6. Расчитываем
        CreditDto creditDto = calculatorClient.calculateCredit(scoringData);
        
        // 7. Билдим кредит из результатов
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
        
        // 8. Связываем заявку с созданным кредитом и обновляем на одобрение
        statement.setCredit(savedCredit);
        statement.setStatus(ApplicationStatus.CC_APPROVED);
        // Запись в историю
        statusHistory.add(StatementStatusHistoryDto.builder()
            .status(ApplicationStatus.CC_APPROVED)
            .time(OffsetDateTime.now())
            .changeType(ChangeType.AUTOMATIC)
            .build());
        
        statementRepository.save(statement);

        
        log.info("Statement updated with credit and CC_APPROVED status");

        log.info("Preparing to send message to Kafka for statementId: {}", statement.getId());

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setAddress(statement.getClient().getEmail());
        emailMessage.setTheme("Документы по кредиту");
        emailMessage.setStatementId(statement.getId().getMostSignificantBits());
        emailMessage.setText("Документы для вашей кредитной карты успешно сформированы");

        kafkaTemplate.send("send-documents", emailMessage);
        log.info("Message sent to Kafka");

            // Обновление статуса на DOCUMENTS_CREATED
        statement.setStatus(ApplicationStatus.DOCUMENTS_CREATED);
        statementRepository.save(statement);
        log.info("Statement status updated to DOCUMENTS_CREATED for statement: {}", statement.getId());
    }



    
}