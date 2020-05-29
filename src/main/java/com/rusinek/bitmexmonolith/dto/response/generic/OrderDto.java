package com.rusinek.bitmexmonolith.dto.response.generic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Adrian Rusinek on 29.05.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends ExchangeDto implements Serializable {

    static final long serialVersionUID = 5840388921054154220L;

    private Long orderQty;
}
