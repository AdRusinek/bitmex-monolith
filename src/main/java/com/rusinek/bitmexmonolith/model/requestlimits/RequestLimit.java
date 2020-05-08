package com.rusinek.bitmexmonolith.model.requestlimits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * Created by Adrian Rusinek on 08.05.2020
 **/
@Getter
@Setter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class RequestLimit {

    private long blockadeActivatedAt;
    private long apiReadyToUse;

}
