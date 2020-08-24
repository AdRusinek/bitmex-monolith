package com.rusinek.bitmexmonolith.exceptions.exceptionHandlers;

import com.rusinek.bitmexmonolith.exceptions.authenticationException.ToManyLoginFailuresException;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.ToManyLoginFailuresExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Adrian Rusinek on 25.08.2020
 **/
@ControllerAdvice
@RestController
public class ToManyLoginFailuresExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleToManyLoginFailures(ToManyLoginFailuresException e) {
        return new ResponseEntity<>(new ToManyLoginFailuresExceptionResponse(e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    }
}
