package com.search.service;

import com.search.dto.FlightRequestDTO;
import com.search.dto.FlightResponseDTO;
import com.search.entity.Flight;
import com.search.exception.DuplicateFlightException;
import com.search.exception.FlightNotFoundException;
import com.search.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.search.exception.ServiceException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FlightService {
    
    private final FlightRepository flightRepository;
    
    
    public void updateAvailableSeats(Long id, int newAvailableSeats) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        flight.setAvailableSeats(newAvailableSeats);
        flightRepository.save(flight);
    }


    
    @Transactional(readOnly = true)
    public List<FlightResponseDTO> getAllFlights() {
        log.info("Fetching all flights");
        try {
            return flightRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all flights", e);
            throw new ServiceException("Failed to fetch flights", e);
        }
    }

    @Transactional(readOnly = true)
    public FlightResponseDTO getFlightById(Long id) {
        log.info("Fetching flight with ID: {}", id);
        try {
            Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Flight not found with ID: {}", id);
                    return new FlightNotFoundException("Flight not found with ID: " + id);
                });
            
            return convertToDto(flight);
        } catch (FlightNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching flight with ID: {}", id, e);
            throw new ServiceException("Failed to fetch flight details", e);
        }
    }

    @Transactional(readOnly = true)
    public List<FlightResponseDTO> searchFlights(String source, String destination, LocalDateTime date) {
        log.info("Searching flights from {} to {} on {}", source, destination, date);
        try {
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
            
            return flightRepository.findBySourceAndDestinationAndDepartureDateBetween(
                    source, 
                    destination, 
                    startOfDay, 
                    endOfDay)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching flights", e);
            throw new ServiceException("Failed to search flights", e);
        }
    }

    public FlightResponseDTO addFlight(FlightRequestDTO request) {
        log.info("Adding new flight: {}", request);
        try {
            if (flightRepository.existsByAirlineAndSourceAndDestinationAndDepartureDate(
                request.getAirline(), 
                request.getSource(), 
                request.getDestination(), 
                request.getDepartureDate())) {
                log.warn("Duplicate flight detected: {}", request);
                throw new DuplicateFlightException("Flight already exists");
            }
            
            Flight flight = convertToEntity(request);
            flight.setAvailableSeats(request.getTotalSeats());
            Flight savedFlight = flightRepository.save(flight);
            log.info("Flight added successfully with ID: {}", savedFlight.getId());
            return convertToDto(savedFlight);
        } catch (DuplicateFlightException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error adding flight", e);
            throw new ServiceException("Failed to add flight", e);
        }
    }

    public FlightResponseDTO updateFlight(Long id, FlightRequestDTO request) {
        log.info("Updating flight ID {} with data: {}", id, request);
        try {
            Flight flight = flightRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Flight not found for update with ID: {}", id);
                        return new FlightNotFoundException("Flight not found with id: " + id);
                    });
            
            flight.setAirline(request.getAirline());
            flight.setSource(request.getSource());
            flight.setDestination(request.getDestination());
            flight.setDepartureDate(request.getDepartureDate());
            
            int seatDifference = request.getTotalSeats() - flight.getTotalSeats();
            flight.setAvailableSeats(flight.getAvailableSeats() + seatDifference);
            flight.setTotalSeats(request.getTotalSeats());
            
            Flight updatedFlight = flightRepository.save(flight);
            log.info("Flight ID {} updated successfully", id);
            return convertToDto(updatedFlight);
        } catch (FlightNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating flight ID: {}", id, e);
            throw new ServiceException("Failed to update flight", e);
        }
    }

    public void deleteFlight(Long id) {
        log.info("Deleting flight with ID: {}", id);
        try {
            if (!flightRepository.existsById(id)) {
                log.warn("Flight not found for deletion with ID: {}", id);
                throw new FlightNotFoundException("Flight not found with id: " + id);
            }
            flightRepository.deleteById(id);
            log.info("Flight ID {} deleted successfully", id);
        } catch (FlightNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting flight ID: {}", id, e);
            throw new ServiceException("Failed to delete flight", e);
        }
    }

    // Conversion methods with null checks
    private FlightResponseDTO convertToDto(Flight flight) {
        if (flight == null) {
            log.warn("Attempted to convert null flight to DTO");
            return null;
        }
        
        return FlightResponseDTO.builder()
                .id(flight.getId())
                .airline(flight.getAirline())
                .source(flight.getSource())
                .destination(flight.getDestination())
                .departureDate(flight.getDepartureDate())
                .availableSeats(flight.getAvailableSeats())
                .totalSeats(flight.getTotalSeats())
                .createdAt(flight.getCreatedAt())
                .build();
    }
    
    private Flight convertToEntity(FlightRequestDTO dto) {
        if (dto == null) {
            log.warn("Attempted to convert null DTO to flight entity");
            return null;
        }
        
        return Flight.builder()
                .airline(dto.getAirline())
                .source(dto.getSource())
                .destination(dto.getDestination())
                .departureDate(dto.getDepartureDate())
                .totalSeats(dto.getTotalSeats())
                .build();
    }
}