package com.rusinek.bitmexmonolith.exceptions.authenticationException;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 01.03.2020
 **/
@Getter
@Setter
public class InvalidLoginResponse {

    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid username";
        this.password = "Invalid password";
    }
}
