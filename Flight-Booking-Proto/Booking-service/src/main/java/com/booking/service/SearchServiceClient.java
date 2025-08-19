package com.booking.service;

import com.search.dto.FlightResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "search-service")
public interface SearchServiceClient {
    @GetMapping("/api/flights/{id}")
    FlightResponseDTO getFlightById(@PathVariable("id") Long id);

    @PutMapping("/api/flights/update-seats/{id}")
    void updateAvailableSeats(@PathVariable("id") Long flightId, @RequestParam("seats") int seats);
}
