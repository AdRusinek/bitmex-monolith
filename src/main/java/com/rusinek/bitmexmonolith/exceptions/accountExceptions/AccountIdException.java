package com.rusinek.bitmexmonolith.exceptions.accountExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 08.03.2020
 **/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountIdException extends RuntimeException {

    public AccountIdException(String message) {
        super(message);
    }
}
