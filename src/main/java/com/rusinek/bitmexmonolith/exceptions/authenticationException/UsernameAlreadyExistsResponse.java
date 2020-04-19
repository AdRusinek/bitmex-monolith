package com.rusinek.bitmexmonolith.exceptions.authenticationException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
@Getter
@Setter
@AllArgsConstructor
public class UsernameAlreadyExistsResponse {

    private String username;
}
