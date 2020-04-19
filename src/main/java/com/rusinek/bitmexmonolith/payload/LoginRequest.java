package com.rusinek.bitmexmonolith.payload;

import javax.validation.constraints.NotBlank;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
public class LoginRequest {

    @NotBlank(message = "Username cannot be blank.")
    private String username;
    @NotBlank(message = "Password cannot be blank.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
