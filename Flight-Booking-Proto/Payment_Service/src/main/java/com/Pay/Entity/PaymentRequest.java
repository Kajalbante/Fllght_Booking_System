package com.Pay.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
	private String upiId = "kajalbante@axl";  // default
    private String name = "FLight Booking System";            // default
    private double amount;
    
    public void setUpiId(String upiId) {
        if (upiId != null && !upiId.isBlank()) {
            this.upiId = upiId;
        }
    }
}
