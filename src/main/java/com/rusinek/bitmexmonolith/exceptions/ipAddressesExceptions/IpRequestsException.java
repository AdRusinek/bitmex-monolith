package com.rusinek.bitmexmonolith.exceptions.ipAddressesExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Adrian Rusinek on 01.06.2020
 **/
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class IpRequestsException extends RuntimeException {

    public IpRequestsException(String message) {
        super(message);
    }

}
