package com.rusinek.bitmexmonolith.exceptions.trailingStopExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 29.05.2020
 **/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TrailingStopAmountException extends RuntimeException {

    public TrailingStopAmountException(String message) {
        super(message);
    }
}
