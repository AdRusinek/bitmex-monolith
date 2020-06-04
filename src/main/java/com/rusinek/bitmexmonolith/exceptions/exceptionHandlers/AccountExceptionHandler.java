package com.rusinek.bitmexmonolith.exceptions.exceptionHandlers;

import com.rusinek.bitmexmonolith.exceptions.accountExceptions.*;
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
public class AccountExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleAccountAmountException(AccountAmountException e) {
        return new ResponseEntity<>(new AccountAmountExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAccountCredentialsException(AccountCredentialsException e) {
        return new ResponseEntity<>(new AccountCredentialsExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
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
}
