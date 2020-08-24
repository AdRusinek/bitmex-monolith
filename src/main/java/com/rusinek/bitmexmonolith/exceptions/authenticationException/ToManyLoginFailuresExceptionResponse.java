package com.rusinek.bitmexmonolith.exceptions.authenticationException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 25.08.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToManyLoginFailuresExceptionResponse {

    private String toManyFailures;
}
