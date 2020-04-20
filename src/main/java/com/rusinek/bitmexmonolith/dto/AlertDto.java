package com.rusinek.bitmexmonolith.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Adrian Rusinek on 20.04.2020
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDto implements Serializable {

    static final long serialVersionUID = 6091365536923453200L;

    private Long id;
    private String alertMessage;
    private Double alertTriggeringPrice;
}
