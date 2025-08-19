package com.pricing.service;

import com.pricing.dto.FareRequestDTO;
import com.pricing.dto.FareResponseDTO;
import com.pricing.entity.FlightPricing;
import com.pricing.repository.PricingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

    private static final Logger logger = LoggerFactory.getLogger(PricingService.class);

    @Autowired
    private PricingRepository pricingRepository;

    public FareResponseDTO calculateFare(FareRequestDTO fareRequest) {
        logger.info("Received fare request: airline={}, source={}, destination={}",
                fareRequest.getAirline(), fareRequest.getSource(), fareRequest.getDestination());

        FlightPricing pricing = pricingRepository
                .findByAirlineAndSourceAndDestination(
                        fareRequest.getAirline(),
                        fareRequest.getSource(),
                        fareRequest.getDestination()
                )
                .orElseThrow(() -> {
                    String errorMsg = String.format(
                            "Pricing not found for airline=%s, source=%s, destination=%s",
                            fareRequest.getAirline(), fareRequest.getSource(), fareRequest.getDestination()
                    );
                    logger.error(errorMsg);
                    return new RuntimeException(errorMsg);
                });

        FareResponseDTO response = new FareResponseDTO();
        response.setBasePrice(pricing.getBasePrice());
        response.setTaxes(pricing.getTaxes());
        response.setTotalFare(pricing.getBasePrice() + pricing.getTaxes());

        logger.info("Calculated fare: basePrice={}, taxes={}, totalFare={}",
                response.getBasePrice(), response.getTaxes(), response.getTotalFare());

        return response;
    }
}
