package com.example.deal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passport")
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "passport_uuid")
    private UUID id;
    
    @Column(nullable = false)
    private String series;
    
    @Column(nullable = false)
    private String number;
}