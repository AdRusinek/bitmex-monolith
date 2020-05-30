package com.rusinek.bitmexmonolith.exceptions.exchangeExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 30.05.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeLimitsExceptionResponse {

    private String limitsExceeded;
}
