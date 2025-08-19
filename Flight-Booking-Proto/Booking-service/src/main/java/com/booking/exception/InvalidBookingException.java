package com.booking.exception;

public class InvalidBookingException extends BookingException {
    public InvalidBookingException(String message) {
        super(message);
    }
    
    public InvalidBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}