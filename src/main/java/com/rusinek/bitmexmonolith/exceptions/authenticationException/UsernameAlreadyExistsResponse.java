package com.rusinek.bitmexmonolith.exceptions.authenticationException;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
public class UsernameAlreadyExistsResponse {

    private String username;

    public UsernameAlreadyExistsResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
