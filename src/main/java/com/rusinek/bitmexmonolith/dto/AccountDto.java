package com.rusinek.bitmexmonolith.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto implements Serializable {

    static final long serialVersionUID = 6235791575323075608L;

    private Long id;
    private String accountName;
}
