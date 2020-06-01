package com.rusinek.bitmexmonolith.model.limits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * Created by Adrian Rusinek on 01.06.2020
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class IpAddressRequestLimit extends RequestLimit{

    private String ipAddress;
    private Integer actionAttempts;
}
