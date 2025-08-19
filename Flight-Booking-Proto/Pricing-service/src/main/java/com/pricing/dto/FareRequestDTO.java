package com.pricing.dto;  

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;  
//
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FareRequestDTO {
//	private long flightId;
    private String airline;
    private String source;
    private String destination;
}