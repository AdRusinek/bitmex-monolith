package com.rusinek.bitmexmonolith.dto.response.generic;

import lombok.*;

import java.io.Serializable;

/**
 * Created by Adrian Rusinek on 29.05.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDto implements Serializable {

    static final long serialVersionUID = 1898340432028408682L;

    private String symbol;
}
