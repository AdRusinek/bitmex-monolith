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
public class PositionDto implements Serializable {

    static final long serialVersionUID = -4501317146703715095L;

    private Timestamp openingTimestamp;
    private String symbol;
    private Long openingQty;
    private Float homeNotional;
    private Float avgEntryPrice;
    private Float markPrice;
    private Float liquidationPrice;
    private Float leverage;
}
