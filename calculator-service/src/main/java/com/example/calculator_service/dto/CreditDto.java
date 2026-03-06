package com.example.calculator_service.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Полная информация о кредите")
public class CreditDto {

    @Schema(
        description = "сумма кредита",
        example = "6500"
    )
    private BigDecimal amount;

    @Schema(
        description = "срок крдита в месяцах",
        example = "12"
    )
    private Integer term;

    @Schema(
        description = "Платеж каждый месяц",
        example = "124.00"
    )
    private BigDecimal monthlyPayment;

    @Schema(
         description = "Процентная ставка",
        example = "9.0"
    )
    private BigDecimal rate;

    @Schema(
        description = "Полная стоимость кредита",
       example = "15.5"
   )
    private BigDecimal psk;

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

    @Schema(
        description = "График платежей"
   )
    private List<PaymentScheduleElementDto> paymentSchedule;
}
