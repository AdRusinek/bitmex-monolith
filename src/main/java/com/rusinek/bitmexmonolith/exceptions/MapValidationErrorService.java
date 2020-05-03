package com.rusinek.bitmexmonolith.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrian Rusinek on 24.02.2020
 **/
@Service
public class MapValidationErrorService {

    public ResponseEntity<?> validateErrors(BindingResult result) {

        HashMap<Object,Object> errorM = new HashMap<>();

        for(FieldError error: result.getFieldErrors()){
            errorM.put(error.getField(),error.getDefaultMessage());
        }

        return new ResponseEntity<>(errorM, HttpStatus.BAD_REQUEST);
    }
}