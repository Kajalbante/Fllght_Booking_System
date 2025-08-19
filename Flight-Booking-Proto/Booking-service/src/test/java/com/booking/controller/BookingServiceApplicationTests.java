package com.booking.controller;

import com.booking.dto.BookingRequestDTO;
import com.booking.dto.BookingResponseDTO;
import com.booking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private BookingRequestDTO createValidRequest() {
        return BookingRequestDTO.builder()
                .userId(1L)
                .Id(101L)
                .passengerName("Alice")
                .passengerEmail("alice@example.com")
                .build();
    }

    private BookingResponseDTO createValidResponse() {
        return BookingResponseDTO.builder()
                .userId(1L)
                .airline("Air India")
                .source("Delhi")
                .destination("Bangalore")
                .totalFare(3200.0)
                .passengerName("Alice")
                .status("CONFIRMED")
                .bookingTime(LocalDateTime.now())
                .message("Booking successful")
                .build();
    }

    @Test
    void testBookFlight_Success() {
        BookingRequestDTO request = createValidRequest();
        BookingResponseDTO expectedResponse = createValidResponse();

        when(bookingService.bookFlight(request)).thenReturn(expectedResponse);

        ResponseEntity<BookingResponseDTO> response = bookingController.bookFlight(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("CONFIRMED", response.getBody().getStatus());
        assertEquals("Alice", response.getBody().getPassengerName());
        verify(bookingService, times(1)).bookFlight(request);
    }


    @Test
    void testBookFlight_MissingEmail() {
        BookingRequestDTO request = BookingRequestDTO.builder()
                .userId(2L)
                .Id(202L)
                .passengerName("Bob")
                .passengerEmail(null) // Missing email
                .build();

        BookingResponseDTO responseDTO = createValidResponse();
        responseDTO.setPassengerName("Bob");

        when(bookingService.bookFlight(request)).thenReturn(responseDTO);

        ResponseEntity<BookingResponseDTO> response = bookingController.bookFlight(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Bob", response.getBody().getPassengerName());
        verify(bookingService).bookFlight(request);
    }

    @Test
    void testBookFlight_InvalidUserId() {
        BookingRequestDTO request = BookingRequestDTO.builder()
                .userId(null)
                .Id(103L)
                .passengerName("Chris")
                .passengerEmail("chris@example.com")
                .build();

        BookingResponseDTO responseDTO = createValidResponse();
        responseDTO.setPassengerName("Chris");

        when(bookingService.bookFlight(request)).thenReturn(responseDTO);

        ResponseEntity<BookingResponseDTO> response = bookingController.bookFlight(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Chris", response.getBody().getPassengerName());
        verify(bookingService).bookFlight(request);
    }

    @Test
    void testBookFlight_ServiceThrowsException() {
        BookingRequestDTO request = createValidRequest();

        when(bookingService.bookFlight(request)).thenThrow(new RuntimeException("Flight not found"));

        assertThrows(RuntimeException.class, () -> {
            bookingController.bookFlight(request);
        });

        verify(bookingService).bookFlight(request);
    }

    @Test
    void testBookFlight_EmptyPassengerName() {
        BookingRequestDTO request = BookingRequestDTO.builder()
                .userId(5L)
                .Id(105L)
                .passengerName("") // Empty name
                .passengerEmail("emptyname@example.com")
                .build();

        BookingResponseDTO responseDTO = createValidResponse();
        responseDTO.setPassengerName("");

        when(bookingService.bookFlight(request)).thenReturn(responseDTO);

        ResponseEntity<BookingResponseDTO> response = bookingController.bookFlight(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("", response.getBody().getPassengerName());
        verify(bookingService).bookFlight(request);
    }
}
