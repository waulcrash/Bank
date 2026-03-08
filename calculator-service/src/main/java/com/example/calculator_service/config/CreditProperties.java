package com.example.calculator_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "credit.calculator")
@Data
public class CreditProperties {
    
    private BigDecimal baseRate = BigDecimal.valueOf(15.0);
    
    private Insurance insurance = new Insurance();
    private Salary salary = new Salary();
    
    @Data
    public static class Insurance {
        private BigDecimal discount = BigDecimal.valueOf(3.0);
        private BigDecimal costPerMillion = BigDecimal.valueOf(100000);
    }
    
    @Data
    public static class Salary {
        private BigDecimal discount = BigDecimal.valueOf(1.0);
    }
}