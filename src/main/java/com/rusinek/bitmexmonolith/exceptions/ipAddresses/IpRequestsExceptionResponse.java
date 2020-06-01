package com.rusinek.bitmexmonolith.exceptions.ipAddresses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adrian Rusinek on 01.06.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IpRequestsExceptionResponse {

    private String requestFromIp;
}
