package com.rusinek.bitmexmonolith.dto.response;

import com.rusinek.bitmexmonolith.dto.response.generic.OrderDto;
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
public class OrderLimitDto extends OrderDto implements Serializable {

    static final long serialVersionUID = -2903371861987229582L;

    private Timestamp timestamp;
    private Float price;
    private Long leavesQty;
}
