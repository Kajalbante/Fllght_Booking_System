package com.booking.service;

import com.booking.dto.BookingRequestDTO;
import com.booking.dto.BookingResponseDTO;
import com.booking.entity.Booking;
import com.booking.repository.BookingRepository;
import com.pricing.dto.FareRequestDTO;
import com.pricing.dto.FareResponseDTO;
import com.search.dto.FlightResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PricingServiceClient pricingClient;
    private final SearchServiceClient searchClient;

    public BookingResponseDTO bookFlight(BookingRequestDTO requestDTO) {
        log.info("Initiating booking for user ID: {}, flight ID: {}", requestDTO.getUserId(), requestDTO.getId());

        FlightResponseDTO flight;
        try {
            flight = searchClient.getFlightById(requestDTO.getId());
            log.info("Fetched flight details: {}", flight);
        } catch (Exception e) {
            log.error("Failed to fetch flight details for ID: {}", requestDTO.getId(), e);
            throw new RuntimeException("Flight retrieval failed", e);
        }

        if (flight.getAvailableSeats() <= 0) {
            log.warn("No available seats for flight ID: {}", requestDTO.getId());
            throw new RuntimeException("No seats available.");
        }

        FareResponseDTO fare;
        try {
            FareRequestDTO fareRequest = FareRequestDTO.builder()
                    .airline(flight.getAirline())
                    .source(flight.getSource())
                    .destination(flight.getDestination())
                    .build();

            fare = pricingClient.calculateFare(fareRequest);
            log.info("Fare calculated: {}", fare);
        } catch (Exception e) {
            log.error("Failed to calculate fare for flight ID: {}", requestDTO.getId(), e);
            throw new RuntimeException("Fare calculation failed", e);
        }

        Booking newBooking = new Booking();
        newBooking.setUserId(requestDTO.getUserId());
        newBooking.setAirline(flight.getAirline());
        newBooking.setSource(flight.getSource());
        newBooking.setDestination(flight.getDestination());
        newBooking.setDepartureDate(flight.getDepartureDate());
        newBooking.setBasePrice(fare.getBasePrice());
        newBooking.setTaxes(fare.getTaxes());
        newBooking.setTotalFare(fare.getTotalFare());
        newBooking.setPassengerName(requestDTO.getPassengerName());
        newBooking.setPassengerEmail(requestDTO.getPassengerEmail());
        newBooking.setStatus("CONFIRMED");
        newBooking.setBookingTime(LocalDateTime.now());
        newBooking.setLastModified(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(newBooking);
        log.info("Booking saved successfully with ID: {}", savedBooking.getId());

        try {
            searchClient.updateAvailableSeats(requestDTO.getId(), flight.getAvailableSeats() - 1);
            log.info("Updated available seats for flight ID: {}", requestDTO.getId());
        } catch (Exception e) {
            log.error("Failed to update available seats for flight ID: {}", requestDTO.getId(), e);
            throw new RuntimeException("Failed to update available seats", e);
        }

        BookingResponseDTO response = BookingResponseDTO.builder()
                .airline(savedBooking.getAirline())
                .source(savedBooking.getSource())
                .destination(savedBooking.getDestination())
                .totalFare(savedBooking.getTotalFare())
                .passengerName(savedBooking.getPassengerName())
                .status(savedBooking.getStatus())
                .bookingTime(savedBooking.getBookingTime())
                .message("Booking successful")
                .build();

        log.info("Booking completed successfully for user ID: {}, booking ID: {}", requestDTO.getUserId(), savedBooking.getId());
        return response;
    }
}
