package com.rusinek.bitmexmonolith.exceptions;


import com.rusinek.bitmexmonolith.exceptions.accountExceptions.*;
import com.rusinek.bitmexmonolith.exceptions.alertException.AlertAlreadyExistsException;
import com.rusinek.bitmexmonolith.exceptions.alertException.AlertAlreadyExistsResponse;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsException;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsResponse;
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
    public final ResponseEntity<Object> handleAlertAlreadyExistsException(AlertAlreadyExistsException e) {
        return new ResponseEntity<>(new AlertAlreadyExistsResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
