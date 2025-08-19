//package com.booking.controller;
//
//import com.booking.dto.*;
//import com.booking.service.BookingService;
//import com.pricing.dto.FareRequestDTO;
//import com.pricing.dto.FareResponseDTO;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/bookings")
//public class BookingController {
//
//    @Autowired
//    private BookingService bookingService;
//
//    @PostMapping("/calculate-fare")
//    public ResponseEntity<FareResponseDTO> calculateFare(@RequestBody FareRequestDTO fareRequest) {
//        return ResponseEntity.ok(bookingService.getFareDetails(fareRequest));
//    }
//
//    @PostMapping
//    public ResponseEntity<BookingResponseDTO> createBooking(
//            @RequestBody BookingRequestDTO bookingRequest,
//            @AuthenticationPrincipal Long userId) {
//        return ResponseEntity.ok(bookingService.createBooking(bookingRequest, userId));
// 
//    }
//}
package com.booking.controller;

import com.booking.dto.BookingRequestDTO;
import com.booking.dto.BookingResponseDTO;
import com.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3002") 
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> bookFlight(@RequestBody BookingRequestDTO request) {
        BookingResponseDTO response = bookingService.bookFlight(request);
        return ResponseEntity.ok(response);
    }
}
