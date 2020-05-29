package com.rusinek.bitmexmonolith.exceptions.trailingStopExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 29.05.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrailingStopAmountExceptionResponse {

    private String trailingStopAmount;
}
