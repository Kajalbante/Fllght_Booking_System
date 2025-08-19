package com.pricing.dto;  

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;  

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FareResponseDTO {
	private long flightId;
    private Double basePrice;
    private Double taxes;
    private Double totalFare;
}  