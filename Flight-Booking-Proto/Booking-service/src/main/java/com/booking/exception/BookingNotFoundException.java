package com.booking.exception;

public class BookingNotFoundException extends BookingException {
    public BookingNotFoundException(Long bookingId) {
        super("Booking not found with ID: " + bookingId);
    }
}