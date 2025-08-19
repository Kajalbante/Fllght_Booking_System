package com.search.controller;

import com.search.dto.FlightRequestDTO;
import com.search.dto.FlightResponseDTO;
import com.search.entity.Flight;
import com.search.service.FlightService;
import com.search.repository.FlightRepository; // Added missing import
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001") 
public class FlightController {
    
    private final FlightService flightService;
    private final FlightRepository flightRepository; // Added missing dependency
    
    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getFlightById(@PathVariable Long id) {
        FlightResponseDTO flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }
    
    @GetMapping("/debug/{id}")
    public ResponseEntity<?> debugFlight(@PathVariable Long id) {
        try {
            Optional<Flight> raw = flightRepository.findById(id);
            FlightResponseDTO converted = flightService.getFlightById(id);
            return ResponseEntity.ok(Map.of(
                "repositoryResult", raw.orElse(null), // Handle Optional properly
                "serviceResult", converted
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of(
                    "error", e.getMessage(),
                    "stackTrace", e.getStackTrace()
                ));
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        LocalDateTime searchDate = (date != null) ? 
            date.atStartOfDay() : 
            LocalDate.now().atStartOfDay();
        
        return ResponseEntity.ok(flightService.searchFlights(source, destination, searchDate));
    }

    @GetMapping("/search/alt")
    public ResponseEntity<List<FlightResponseDTO>> searchFlightsAlt(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(required = false) String dateStr) {
        
        LocalDateTime searchDate;
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                searchDate = LocalDate.parse(dateStr).atStartOfDay();
            } catch (Exception e) {
                searchDate = LocalDate.now().atStartOfDay();
            }
        } else {
            searchDate = LocalDate.now().atStartOfDay();
        }
        
        return ResponseEntity.ok(flightService.searchFlights(source, destination, searchDate));
    }
    
    @PostMapping
    public ResponseEntity<FlightResponseDTO> addFlight(@Valid @RequestBody FlightRequestDTO flightRequest) {
        FlightResponseDTO response = flightService.addFlight(flightRequest);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(
            @PathVariable Long id,
            @Valid @RequestBody FlightRequestDTO flightRequest) {
        FlightResponseDTO response = flightService.updateFlight(id, flightRequest);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update-seats/{id}")
    public ResponseEntity<Void> updateSeats(@PathVariable Long id, @RequestParam int seats) {
        flightService.updateAvailableSeats(id, seats);
        return ResponseEntity.ok().build();
    }
   

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}