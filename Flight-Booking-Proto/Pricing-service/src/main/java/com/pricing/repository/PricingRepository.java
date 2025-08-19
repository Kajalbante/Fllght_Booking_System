package com.pricing.repository;  

import com.pricing.entity.FlightPricing;  
import org.springframework.data.jpa.repository.JpaRepository;  
import java.util.Optional;  

public interface PricingRepository extends JpaRepository<FlightPricing, Long> {  
	Optional<FlightPricing> findByAirlineAndSourceAndDestination(String airline, String source, String destination);

}  