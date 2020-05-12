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
public class OrderLimitDto implements Serializable {

    static final long serialVersionUID = -2903371861987229582L;

    private Timestamp timestamp;
    private String symbol;
    private Long orderQty;
    private Float price;
    private Long leavesQty;
}
