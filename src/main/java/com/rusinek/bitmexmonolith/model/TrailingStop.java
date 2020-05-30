package com.rusinek.bitmexmonolith.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

/**
 * Created by Adrian Rusinek on 24.02.2020
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TrailingStop extends Stop {

    @NotEmpty(message = "Trial Value is required.")
    private String trialValue;
}
