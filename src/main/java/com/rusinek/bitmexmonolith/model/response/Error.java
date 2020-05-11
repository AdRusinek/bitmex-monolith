package com.rusinek.bitmexmonolith.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Adrian Rusinek on 11.05.2020
 **/
@Getter
@Setter
@ToString
public class Error {

    private String message;
    private String name;
}
