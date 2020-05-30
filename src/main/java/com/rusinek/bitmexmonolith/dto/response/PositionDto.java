package com.rusinek.bitmexmonolith.dto.response;

import com.rusinek.bitmexmonolith.dto.response.generic.ExchangeDto;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Adrian Rusinek on 12.05.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionDto extends ExchangeDto implements Serializable {

    private Timestamp openingTimestamp;
    private Long openingQty;
    private Float homeNotional;
    private Float avgEntryPrice;
    private Float markPrice;
    private Float liquidationPrice;
    private Float leverage;
}
