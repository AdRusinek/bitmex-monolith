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
public class OrderStopDto extends OrderDto implements Serializable {

    private Timestamp timestamp;
    private String price;
}
