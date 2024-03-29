package com.rusinek.bitmexmonolith.dto;

import lombok.*;

import java.io.Serializable;


/**
 * Created by Adrian Rusinek on 04.05.2020
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrailingStopDto implements Serializable {

    static final long serialVersionUID = 487441682142195435L;

    private Long id;
    private Long accountId;
    private Double startingPrice;
    private Double quantity;
    private String trialValue;
    private String execInst;
}
