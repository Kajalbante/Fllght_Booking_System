package com.pricing.entity;  

import jakarta.persistence.*;  
import lombok.Data;  
import java.time.LocalDateTime;

@Entity
@Table(name = "fare")  // Must match your table name
@Data
public class FlightPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false) // Match your table constraints
    private String airline;
    
    @Column(nullable = false)
    private String source;
    
    @Column(nullable = false)
    private String destination;
    
    @Column(name = "base_price", nullable = false)
    private Double basePrice;
    
    @Column(nullable = false)
    private Double taxes;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}