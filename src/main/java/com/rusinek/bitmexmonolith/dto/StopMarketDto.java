package com.rusinek.bitmexmonolith.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StopMarketDto implements Serializable {

    static final long serialVersionUID = -7103958819832568909L;

    private Long id;
    private Long accountId;
    private Double startingPrice;
    private Double quantity;
    private Float stopPrice;
    private String execInst;
}
