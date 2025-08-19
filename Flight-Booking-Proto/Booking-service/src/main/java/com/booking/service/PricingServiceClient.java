
package com.booking.service;

import com.pricing.dto.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "pricing-service")
public interface PricingServiceClient {
    @PostMapping("/api/pricing/calculate-fare")
    FareResponseDTO calculateFare(@RequestBody FareRequestDTO fareRequest);
}