package com.example.calculator_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Кредитные предложения")
public class LoanOfferDto {

    @Schema(
        description = "UUID заявки"
   )
    private UUID statementId;

    @Schema(
        description = "Запрашиваемая сумма",
       example = "6500"
   )
    private BigDecimal requestedAmount;

    @Schema(
        description = "Итоговая сумма",
       example = "7000"
   )
    private BigDecimal totalAmount;

    @Schema(
        description = "Срок кредита",
       example = "12"
   )
    private Integer term;

    @Schema(
        description = "Кжемесячный платеж",
       example = "600"
   )
    private BigDecimal monthlyPayment;

    @Schema(
        description = "Процентная ставка",
       example = "9.0"
   )
    private BigDecimal rate;

    @Schema(
        description = "Наличие страховки",
       example = "true"
   )
    private boolean isInsuranceEnabled;

    @Schema(
        description = "Клиент способен оплачивать",
       example = "true"
   )
    private boolean isSalaryClient;    
}
