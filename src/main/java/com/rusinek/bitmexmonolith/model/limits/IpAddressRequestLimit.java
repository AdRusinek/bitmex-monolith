package com.rusinek.bitmexmonolith.model.limits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * Created by Adrian Rusinek on 01.06.2020
 **/
@Getter
@Setter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
class IpAddressRequestLimit extends RequestLimit {

    private String ipAddress;
    private Integer actionAttempts;
}
