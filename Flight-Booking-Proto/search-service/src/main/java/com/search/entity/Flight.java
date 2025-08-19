package com.search.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Airline is required")
    @Size(max = 100, message = "Airline cannot exceed 100 characters")
    private String airline;

    @NotBlank(message = "Source is required")
    @Size(max = 100, message = "Source cannot exceed 100 characters")
    private String source;

    @NotBlank(message = "Destination is required")
    @Size(max = 100, message = "Destination cannot exceed 100 characters")
    private String destination;

    @NotNull(message = "Departure date is required")
    @FutureOrPresent(message = "Departure date must be in the present or future")
    private LocalDateTime departureDate;

    @NotNull(message = "Available seats is required")
    @PositiveOrZero(message = "Available seats cannot be negative")
    private Integer availableSeats;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}