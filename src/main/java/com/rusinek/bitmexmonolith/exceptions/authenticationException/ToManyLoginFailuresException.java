package com.rusinek.bitmexmonolith.exceptions.authenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 25.08.2020
 **/
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class ToManyLoginFailuresException extends RuntimeException {

    public ToManyLoginFailuresException(String message) {
        super(message);
    }
}
