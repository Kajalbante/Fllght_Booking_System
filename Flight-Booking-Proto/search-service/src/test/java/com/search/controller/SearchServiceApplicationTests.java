package com.search.controller;

import com.search.dto.FlightRequestDTO;
import com.search.dto.FlightResponseDTO;
import com.search.entity.Flight;
import com.search.repository.FlightRepository;
import com.search.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test Case 1: Get All Flights
    @Test
    void testGetAllFlights() {
        List<FlightResponseDTO> flightList = List.of(
                new FlightResponseDTO(1L, "Airline 1", "Source 1", "Destination 1", LocalDateTime.now(), 100, 200, LocalDateTime.now())
        );

        when(flightService.getAllFlights()).thenReturn(flightList);

        ResponseEntity<List<FlightResponseDTO>> response = flightController.getAllFlights();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(flightService, times(1)).getAllFlights();
    }

    // Test Case 2: Get Flight by ID
    @Test
    void testGetFlightById() {
        Long flightId = 1L;
        FlightResponseDTO flightResponse = new FlightResponseDTO(1L, "Airline 1", "Source 1", "Destination 1", LocalDateTime.now(), 100, 200, LocalDateTime.now());

        when(flightService.getFlightById(flightId)).thenReturn(flightResponse);

        ResponseEntity<FlightResponseDTO> response = flightController.getFlightById(flightId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(flightId, response.getBody().getId());
        verify(flightService, times(1)).getFlightById(flightId);
    }

    // Test Case 3: Debug Flight (Successful)
    @Test
    void testDebugFlight() {
        Long flightId = 1L;
        Flight flight = new Flight(1L, "Airline 1", "Source 1", "Destination 1", LocalDateTime.now(), 100, 200, LocalDateTime.now());
        FlightResponseDTO flightResponseDTO = new FlightResponseDTO(1L, "Airline 1", "Source 1", "Destination 1", LocalDateTime.now(), 100, 200, LocalDateTime.now());

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(flightService.getFlightById(flightId)).thenReturn(flightResponseDTO);

        ResponseEntity<?> response = flightController.debugFlight(flightId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("repositoryResult"));
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("serviceResult"));
        verify(flightRepository, times(1)).findById(flightId);
        verify(flightService, times(1)).getFlightById(flightId);
    }

    // Test Case 4: Search Flights (With Date)
    @Test
    void testSearchFlightsWithDate() {
        String source = "Source 1";
        String destination = "Destination 1";
        LocalDate date = LocalDate.now();
        LocalDateTime searchDate = date.atStartOfDay();
        List<FlightResponseDTO> flights = List.of(new FlightResponseDTO(1L, "Airline 1", source, destination, searchDate, 100, 200, LocalDateTime.now()));

        when(flightService.searchFlights(source, destination, searchDate)).thenReturn(flights);

        ResponseEntity<List<FlightResponseDTO>> response = flightController.searchFlights(source, destination, date);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(flightService, times(1)).searchFlights(source, destination, searchDate);
    }

    // Test Case 5: Search Flights (Without Date)
    @Test
    void testSearchFlightsWithoutDate() {
        String source = "Source 1";
        String destination = "Destination 1";
        LocalDateTime searchDate = LocalDate.now().atStartOfDay();
        List<FlightResponseDTO> flights = List.of(new FlightResponseDTO(1L, "Airline 1", source, destination, searchDate, 100, 200, LocalDateTime.now()));

        when(flightService.searchFlights(source, destination, searchDate)).thenReturn(flights);

        ResponseEntity<List<FlightResponseDTO>> response = flightController.searchFlights(source, destination, null);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(flightService, times(1)).searchFlights(source, destination, searchDate);
    }

    // Test Case 6: Add Flight
    @Test
    void testAddFlight() {
        FlightRequestDTO flightRequest = new FlightRequestDTO("Airline 1", "Source 1", "Destination 1", LocalDateTime.now(), 200);
        FlightResponseDTO flightResponse = new FlightResponseDTO(1L, "Airline 1", "Source 1", "Destination 1", LocalDateTime.now(), 200, 200, LocalDateTime.now());

        when(flightService.addFlight(flightRequest)).thenReturn(flightResponse);

        ResponseEntity<FlightResponseDTO> response = flightController.addFlight(flightRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Airline 1", response.getBody().getAirline());
        verify(flightService, times(1)).addFlight(flightRequest);
    }

    // Test Case 7: Update Flight
    @Test
    void testUpdateFlight() {
        Long flightId = 1L;
        FlightRequestDTO flightRequest = new FlightRequestDTO("Updated Airline", "Source 1", "Destination 1", LocalDateTime.now(), 150);
        FlightResponseDTO flightResponse = new FlightResponseDTO(flightId, "Updated Airline", "Source 1", "Destination 1", LocalDateTime.now(), 150, 200, LocalDateTime.now());

        when(flightService.updateFlight(flightId, flightRequest)).thenReturn(flightResponse);

        ResponseEntity<FlightResponseDTO> response = flightController.updateFlight(flightId, flightRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Updated Airline", response.getBody().getAirline());
        verify(flightService, times(1)).updateFlight(flightId, flightRequest);
    }

    // Test Case 8: Delete Flight
    @Test
    void testDeleteFlight() {
        Long flightId = 1L;

        doNothing().when(flightService).deleteFlight(flightId);

        ResponseEntity<Void> response = flightController.deleteFlight(flightId);

        assertEquals(204, response.getStatusCodeValue());
        verify(flightService, times(1)).deleteFlight(flightId);
    }

    // Test Case 9: Update Available Seats
    @Test
    void testUpdateSeats() {
        Long flightId = 1L;
        int newSeats = 50;

        doNothing().when(flightService).updateAvailableSeats(flightId, newSeats);

        ResponseEntity<Void> response = flightController.updateSeats(flightId, newSeats);

        assertEquals(200, response.getStatusCodeValue());
        verify(flightService, times(1)).updateAvailableSeats(flightId, newSeats);
    }
}
