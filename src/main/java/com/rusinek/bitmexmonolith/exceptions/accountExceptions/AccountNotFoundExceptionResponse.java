package com.rusinek.bitmexmonolith.exceptions.accountExceptions;

/**
 * Created by Adrian Rusinek on 08.03.2020
 **/
public class AccountNotFoundExceptionResponse {

    private String accountNotFound;

    public AccountNotFoundExceptionResponse(String accountNotFound) {
        this.accountNotFound = accountNotFound;
    }

    public String getAccountNotFound() {
        return accountNotFound;
    }

    public void setAccountNotFound(String accountNotFound) {
        this.accountNotFound = accountNotFound;
    }
}
