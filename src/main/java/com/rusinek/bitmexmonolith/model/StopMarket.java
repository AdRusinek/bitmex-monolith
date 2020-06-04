package com.rusinek.bitmexmonolith.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Adrian Rusinek on 30.05.2020
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StopMarket extends Stop {

    @NotNull(message = "'Stop price' jest wymagane.")
    private Float stopPrice;
    @NotBlank(message = "stopType is necessary.")
    private String stopType;
}
