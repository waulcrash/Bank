package com.example.calculator_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "'Элемент графика платежей")
public class PaymentScheduleElementDto {

    @Schema(
        description = "Номер платежа по порядку",
       example = "1"
   )
    private Integer number;

    @Schema(
        description = "Дата платежа",
       example = "2026-03-12"
   )
    private LocalDate date;

    @Schema(
        description = "Общая сумма платежа",
       example = "7000.00"
   )
    private BigDecimal totalPayment;

    @Schema(
        description = "Сумма процентов в платеже",
       example = "528.00"
   )
    private BigDecimal interestPayment;

    @Schema(
        description = "Сумма погашения основного долга",
       example = "666.00"
   )
    private BigDecimal debtPayment;

    @Schema(
        description = "Остаток долга после платежа",
       example = "723.00"
   )
    private BigDecimal remainingDebt;
}
