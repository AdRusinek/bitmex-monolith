package com.rusinek.bitmexmonolith.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStopDto implements Serializable {

    static final long serialVersionUID = 8900690300114564902L;

    private Timestamp timestamp;
    private String symbol;
    private Long orderQty;
    private String price;
}
