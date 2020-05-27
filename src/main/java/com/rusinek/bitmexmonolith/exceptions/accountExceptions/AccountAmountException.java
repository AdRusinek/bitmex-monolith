package com.rusinek.bitmexmonolith.exceptions.accountExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 26.05.2020
 **/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountAmountException extends RuntimeException {

    public AccountAmountException(String message) {
        super(message);
    }
}
