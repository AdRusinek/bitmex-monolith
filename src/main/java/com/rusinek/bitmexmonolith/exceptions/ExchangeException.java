package com.rusinek.bitmexmonolith.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
@Getter
@Setter
@Builder
public class ExchangeException {
    private String message;
    private String name;
}
