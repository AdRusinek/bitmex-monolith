package com.rusinek.bitmexmonolith.exceptions.accountExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 06.05.2020
 **/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountNameAlreadyExistsException extends RuntimeException {

    public AccountNameAlreadyExistsException(String message) {
        super(message);
    }
}
