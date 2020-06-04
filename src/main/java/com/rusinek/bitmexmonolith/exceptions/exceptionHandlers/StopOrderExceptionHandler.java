package com.rusinek.bitmexmonolith.exceptions.exceptionHandlers;

import com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions.StopMarketAmountException;
import com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions.StopMarketAmountExceptionResponse;
import com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions.TrailingStopAmountException;
import com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions.TrailingStopAmountExceptionResponse;
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
public class StopOrderExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleTrailingStopAmountException(TrailingStopAmountException e) {
        return new ResponseEntity<>(new TrailingStopAmountExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleStopMarketAmountException(StopMarketAmountException e) {
        return new ResponseEntity<>(new StopMarketAmountExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
