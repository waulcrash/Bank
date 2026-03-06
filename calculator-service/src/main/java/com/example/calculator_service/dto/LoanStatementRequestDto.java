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
@Schema(description = "Запрос на предварительный расчет кредита")
public class LoanStatementRequestDto {

    @Schema(
        description = "Запрашиваемая сумма",
       example = "6500",
       required = true
   )
    private BigDecimal amount;

    @Schema(
        description = "Срок кредита в месяцах",
       example = "12",
       required = true
   )
    private Integer term;

    @Schema(
        description = " Имя",
       example = "Павел",
       required = true
   )
    private String firstName;

    @Schema(
        description = "Фамилия",
       example = "Хвостунов",
       required = true
   )
    private String lastName;

    @Schema(
        description = "Отчество",
       example = "Сергеевич"
   )
    private String middleName;

    @Schema(
        description = "Email",
       example = "cozyliux@example.com",
       required = true
   )
    private String email;

    @Schema(
        description = "Дата рождения",
       example = "2005-08-02",
       required = true
   )
    private LocalDate birthdate;

    @Schema(
        description = "Серия паспорта",
       example = "1234",
       required = true
   )
    private String passportSeries;

    @Schema(
        description = "Номер паспорта",
       example = "123456",
       required = true
   )
    private String passportNumber;
}
