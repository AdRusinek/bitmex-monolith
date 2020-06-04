package com.rusinek.bitmexmonolith.exceptions.exceptionHandlers;

import com.rusinek.bitmexmonolith.exceptions.exchangeExceptions.ExchangeLimitsException;
import com.rusinek.bitmexmonolith.exceptions.exchangeExceptions.ExchangeLimitsExceptionResponse;
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
public class ExchangeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleExchangeLimitsException(ExchangeLimitsException e) {
        return new ResponseEntity<>(new ExchangeLimitsExceptionResponse(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
