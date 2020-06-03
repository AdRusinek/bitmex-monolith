package com.rusinek.bitmexmonolith.exceptions.stopOrderExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StopMarketAmountExceptionResponse {

    private String stopOrderAmount;
}
