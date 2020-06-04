package com.rusinek.bitmexmonolith.exceptions.exceptionHandlers;

import com.rusinek.bitmexmonolith.exceptions.alertException.*;
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
public class AlertExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleAlertAmountException(AlertAmountException e) {
        return new ResponseEntity<>(new AlertAmountExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAlertAlreadyExistsException(AlertAlreadyExistsException e) {
        return new ResponseEntity<>(new AlertAlreadyExistsResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAlertIdException(AlertIdException e) {
        return new ResponseEntity<>(new AlertIdExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
