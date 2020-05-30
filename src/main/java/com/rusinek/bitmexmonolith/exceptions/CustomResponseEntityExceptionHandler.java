package com.rusinek.bitmexmonolith.exceptions;


import com.rusinek.bitmexmonolith.exceptions.accountExceptions.*;
import com.rusinek.bitmexmonolith.exceptions.alertException.*;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsException;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsResponse;
import com.rusinek.bitmexmonolith.exceptions.exchangeExceptions.ExchangeLimitsException;
import com.rusinek.bitmexmonolith.exceptions.exchangeExceptions.ExchangeLimitsExceptionResponse;
import com.rusinek.bitmexmonolith.exceptions.trailingStopExceptions.TrailingStopAmountException;
import com.rusinek.bitmexmonolith.exceptions.trailingStopExceptions.TrailingStopAmountExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler
    public final ResponseEntity<Object> handleAccountAmountException(AccountAmountException e) {
        return new ResponseEntity<>(new AccountAmountExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAccountCredentialsException(AccountCredentialsException e) {
        return new ResponseEntity<>(new AccountCredentialsExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        UsernameAlreadyExistsResponse existsResponse = new UsernameAlreadyExistsResponse(e.getMessage());
        return new ResponseEntity<>(existsResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAccountIdException(AccountIdException e) {
        return new ResponseEntity<>(new AccountIdExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException e) {
        return new ResponseEntity<>(new AccountNotFoundExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAccountNameAlreadyExistsException(AccountNameAlreadyExistsException e) {
        return new ResponseEntity<>(new AccountNameAlreadyExistsResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler
    public final ResponseEntity<Object> handleTrailingStopAmountException(TrailingStopAmountException e) {
        return new ResponseEntity<>(new TrailingStopAmountExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleExchangeLimitsException(ExchangeLimitsException e) {
        return new ResponseEntity<>(new ExchangeLimitsExceptionResponse(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
