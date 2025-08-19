package com.search.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateFlightException extends RuntimeException {
    public DuplicateFlightException(String message) {
        super(message);
    }
}