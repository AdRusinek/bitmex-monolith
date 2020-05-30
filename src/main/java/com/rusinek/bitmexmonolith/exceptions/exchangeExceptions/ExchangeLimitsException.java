package com.rusinek.bitmexmonolith.exceptions.exchangeExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 30.05.2020
 **/
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ExchangeLimitsException extends RuntimeException {

    public ExchangeLimitsException(String message) {
        super(message);
    }
}
