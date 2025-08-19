package com.search.repository;

import com.search.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    
    // Explicitly declare findById even though it's inherited
    Optional<Flight> findById(Long id);
    
    List<Flight> findBySourceAndDestinationAndDepartureDateBetween(
            String source, 
            String destination, 
            LocalDateTime startDate, 
            LocalDateTime endDate);
    
    boolean existsByAirlineAndSourceAndDestinationAndDepartureDate(
            String airline, 
            String source, 
            String destination, 
            LocalDateTime departureDate);
}