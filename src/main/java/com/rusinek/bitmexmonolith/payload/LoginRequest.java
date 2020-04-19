package com.rusinek.bitmexmonolith.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username cannot be blank.")
    private String username;
    @NotBlank(message = "Password cannot be blank.")
    private String password;
}
