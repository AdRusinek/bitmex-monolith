package com.rusinek.bitmexmonolith.exceptions.alertException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 07.05.2020
 **/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlertAlreadyExistsException extends RuntimeException {

    public AlertAlreadyExistsException(String message) {
        super(message);
    }
}
