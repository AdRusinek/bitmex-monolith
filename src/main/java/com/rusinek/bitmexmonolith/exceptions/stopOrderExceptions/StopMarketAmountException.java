package com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StopMarketAmountException extends RuntimeException {

    public StopMarketAmountException(String message) {
        super(message);
    }
}
