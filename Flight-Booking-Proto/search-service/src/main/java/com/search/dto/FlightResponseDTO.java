package com.search.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightResponseDTO {
    private Long id;
    private String airline;
    private String source;
    private String destination;
    private LocalDateTime departureDate;
    private Integer availableSeats;
    private Integer totalSeats;
    private LocalDateTime createdAt;
}
