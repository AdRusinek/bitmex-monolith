package com.rusinek.bitmexmonolith.exceptions.alertException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 27.05.2020
 **/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlertIdException extends RuntimeException {

    public AlertIdException(String message) {
        super(message);
    }
}
