package com.rusinek.bitmexmonolith.exceptions.alertException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 26.05.2020
 **/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlertAmountException extends RuntimeException {

    public AlertAmountException(String message) {
        super(message);
    }
}
