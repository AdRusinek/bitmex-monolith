package com.rusinek.bitmexmonolith.exceptions.ipAddressesExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestIpRequestsExceptionResponse {

    private String maxGuestIpRequests;
}
