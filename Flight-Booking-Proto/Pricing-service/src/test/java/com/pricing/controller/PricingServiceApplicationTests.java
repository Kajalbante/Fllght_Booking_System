package com.pricing.controller;

import com.pricing.dto.FareRequestDTO;
import com.pricing.dto.FareResponseDTO;
import com.pricing.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PricingControllerTest {

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private PricingController pricingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private FareRequestDTO createValidRequest() {
        return FareRequestDTO.builder()
                .airline("IndiGo")
                .source("Mumbai")
                .destination("Chennai")
                .build();
    }

    private FareResponseDTO createValidResponse() {
        return FareResponseDTO.builder()
                .flightId(101L)
                .basePrice(2500.0)
                .taxes(500.0)
                .totalFare(3000.0)
                .build();
    }

    @Test
    void testCalculateFare_Success() {
        FareRequestDTO request = createValidRequest();
        FareResponseDTO expectedResponse = createValidResponse();

        when(pricingService.calculateFare(request)).thenReturn(expectedResponse);

        FareResponseDTO response = pricingController.calculateFare(request);

        assertNotNull(response);
        assertEquals(101L, response.getFlightId());
        assertEquals(3000.0, response.getTotalFare());
        verify(pricingService, times(1)).calculateFare(request);
    }

    @Test
    void testCalculateFare_EmptySource() {
        FareRequestDTO request = FareRequestDTO.builder()
                .airline("Vistara")
                .source("") // Empty source
                .destination("Delhi")
                .build();

        FareResponseDTO responseDTO = createValidResponse();

        when(pricingService.calculateFare(request)).thenReturn(responseDTO);

        FareResponseDTO response = pricingController.calculateFare(request);

        assertNotNull(response);
        assertEquals(3000.0, response.getTotalFare());
        verify(pricingService).calculateFare(request);
    }

    @Test
    void testCalculateFare_ServiceThrowsException() {
        FareRequestDTO request = createValidRequest();

        when(pricingService.calculateFare(request)).thenThrow(new RuntimeException("Pricing error"));

        assertThrows(RuntimeException.class, () -> {
            pricingController.calculateFare(request);
        });

        verify(pricingService).calculateFare(request);
    }
    
    @Test
    void testCalculateFare_InvalidDestination() {
        FareRequestDTO request = FareRequestDTO.builder()
                .airline("SpiceJet")
                .source("Delhi")
                .destination("") // Invalid (empty) destination
                .build();

        FareResponseDTO responseDTO = createValidResponse();

        when(pricingService.calculateFare(request)).thenReturn(responseDTO);

        FareResponseDTO response = pricingController.calculateFare(request);

        assertNotNull(response);
        assertEquals(3000.0, response.getTotalFare());
        verify(pricingService).calculateFare(request);
    }

    @Test
    void testCalculateFare_NullAirline() {
        FareRequestDTO request = FareRequestDTO.builder()
                .airline(null) // Null airline
                .source("Bangalore")
                .destination("Goa")
                .build();

        FareResponseDTO responseDTO = createValidResponse();

        when(pricingService.calculateFare(request)).thenReturn(responseDTO);

        FareResponseDTO response = pricingController.calculateFare(request);

        assertNotNull(response);
        assertEquals(3000.0, response.getTotalFare());
        verify(pricingService).calculateFare(request);
    }

    @Test
    void testCalculateFare_ResponseWithZeroFare() {
        FareRequestDTO request = createValidRequest();

        FareResponseDTO zeroFareResponse = FareResponseDTO.builder()
                .flightId(999L)
                .basePrice(0.0)
                .taxes(0.0)
                .totalFare(0.0)
                .build();

        when(pricingService.calculateFare(request)).thenReturn(zeroFareResponse);

        FareResponseDTO response = pricingController.calculateFare(request);

        assertNotNull(response);
        assertEquals(0.0, response.getTotalFare());
        assertEquals(999L, response.getFlightId());
    }

    @Test
    void testCalculateFare_NegativeFareReturned() {
        FareRequestDTO request = createValidRequest();

        FareResponseDTO negativeFareResponse = FareResponseDTO.builder()
                .flightId(404L)
                .basePrice(-100.0)
                .taxes(-50.0)
                .totalFare(-150.0)
                .build();

        when(pricingService.calculateFare(request)).thenReturn(negativeFareResponse);

        FareResponseDTO response = pricingController.calculateFare(request);

        assertNotNull(response);
        assertTrue(response.getTotalFare() < 0, "Total fare should be negative for this test");
        assertEquals(-150.0, response.getTotalFare());
    }

    @Test
    void testCalculateFare_ServiceReturnsNull() {
        FareRequestDTO request = createValidRequest();

        when(pricingService.calculateFare(request)).thenReturn(null);

        FareResponseDTO response = pricingController.calculateFare(request);

        assertNull(response, "Expected null response when service returns null");
        verify(pricingService).calculateFare(request);
    }

}
