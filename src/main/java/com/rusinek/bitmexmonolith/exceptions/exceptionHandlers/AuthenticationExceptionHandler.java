package com.rusinek.bitmexmonolith.exceptions.exceptionHandlers;

import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsException;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsResponse;
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
public class AuthenticationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        UsernameAlreadyExistsResponse existsResponse = new UsernameAlreadyExistsResponse(e.getMessage());
        return new ResponseEntity<>(existsResponse, HttpStatus.BAD_REQUEST);
    }
}
