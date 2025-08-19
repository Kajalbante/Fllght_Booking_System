package com.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingRequestDTO {
    private Long userId;
    private Long Id; // ✅ Needed to retrieve flight details from search-service
    private String passengerName;
    private String passengerEmail;
}
