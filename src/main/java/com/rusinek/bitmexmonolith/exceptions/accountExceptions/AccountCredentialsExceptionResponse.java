package com.rusinek.bitmexmonolith.exceptions.accountExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 26.05.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCredentialsExceptionResponse {

    private String credentialsRepeated;
}
