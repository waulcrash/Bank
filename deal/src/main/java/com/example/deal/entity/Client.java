package com.example.deal.entity;

import com.example.deal.dto.PassportDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "client_id")
    private UUID id;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private String firstName;
    
    private String middleName;
    
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    @Column(nullable = false)
    private String email;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private PassportDto passport;  // Используем сгенерированный PassportDto
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Statement> statements;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}