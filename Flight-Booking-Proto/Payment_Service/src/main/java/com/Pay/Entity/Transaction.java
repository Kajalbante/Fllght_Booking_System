package com.Pay.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "payment_orders")
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razorpayOrderId;
    private String receipt;
    private double amount;
    private String currency;
    private String status;
    private int attempts;
    private LocalDateTime createdAt;
    
	public Transaction() {
		super();
	}
    
    

    // Getters and Setters
}
