package com.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String airline;
    private String source;
    private String destination;

    private LocalDateTime departureDate;

    private Double basePrice;
    private Double taxes;
    private Double totalFare;

    private String passengerName;
    private String passengerEmail;

    private String status; 

    private LocalDateTime bookingTime;
    private LocalDateTime lastModified;
}
