package com.rusinek.bitmexmonolith.exceptions.accountExceptions;

/**
 * Created by Adrian Rusinek on 08.03.2020
 **/
public class AccountIdExceptionResponse {

    private String accountId;

    public AccountIdExceptionResponse(String credentialId) {
        this.accountId = accountId;
    }

    public String getCredentialId() {
        return accountId;
    }

    public void setCredentialId(String credentialId) {
        this.accountId = accountId;
    }
}