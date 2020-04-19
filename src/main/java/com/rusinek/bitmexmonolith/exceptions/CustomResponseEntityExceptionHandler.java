package com.rusinek.bitmexmonolith.exceptions;


import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountIdException;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountIdExceptionResponse;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountNotFoundException;
import com.rusinek.bitmexmonolith.exceptions.accountExceptions.AccountNotFoundExceptionResponse;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsException;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler
    public final ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex, WebRequest request) {
        UsernameAlreadyExistsResponse existsResponse = new UsernameAlreadyExistsResponse(ex.getMessage());
        return new ResponseEntity<>(existsResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleCredentialIdException(AccountIdException ex) {
        return new ResponseEntity<>(new AccountIdExceptionResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleCredentialNotFoundException(AccountNotFoundException ex) {
        return new ResponseEntity<>(new AccountNotFoundExceptionResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
