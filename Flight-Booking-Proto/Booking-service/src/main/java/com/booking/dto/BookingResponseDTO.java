package com.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long userId;
//    private String flightNumber;
    private String source;
    private String airline;
    private String destination;
    private Double totalFare;
    private String passengerName;
    private String status;
    private LocalDateTime bookingTime;
    private String message;
}