package com.pricing.controller;  

import com.pricing.dto.FareRequestDTO;  
import com.pricing.dto.FareResponseDTO;  
import com.pricing.service.PricingService;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.bind.annotation.*;  

@RestController  
@RequestMapping("/api/pricing")  
public class PricingController {  

    @Autowired  
    private PricingService pricingService;  

    @PostMapping("/calculate-fare")  
    public FareResponseDTO calculateFare(@RequestBody FareRequestDTO request) {  
        return pricingService.calculateFare(request);  
    }  
}  