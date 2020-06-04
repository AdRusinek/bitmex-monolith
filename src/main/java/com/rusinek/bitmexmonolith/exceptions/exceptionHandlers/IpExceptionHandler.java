package com.rusinek.bitmexmonolith.exceptions.exceptionHandlers;

import com.rusinek.bitmexmonolith.exceptions.ipAddressesExceptions.GuestIpRequestsException;
import com.rusinek.bitmexmonolith.exceptions.ipAddressesExceptions.GuestIpRequestsExceptionResponse;
import com.rusinek.bitmexmonolith.exceptions.ipAddressesExceptions.UserIpRequestsException;
import com.rusinek.bitmexmonolith.exceptions.ipAddressesExceptions.UserIpRequestsExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@ControllerAdvice
@RestController
public class IpExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleUserIpRequestsException(UserIpRequestsException e) {
        return new ResponseEntity<>(new UserIpRequestsExceptionResponse(e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    }


    @ExceptionHandler
    public final ResponseEntity<Object> handleGuestIpRequestsException(GuestIpRequestsException e) {
        return new ResponseEntity<>(new GuestIpRequestsExceptionResponse(e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    }
}
