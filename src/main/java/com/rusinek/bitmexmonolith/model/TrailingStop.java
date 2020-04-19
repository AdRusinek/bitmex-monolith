package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Adrian Rusinek on 24.02.2020
 **/
@Data
@Entity
public class TrailingStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Starting price is required.")
    private Double startingPrice;
    @NotNull(message = "Quantity is required.")
    private Double quantity;
    @NotNull(message = "Trial Value is required.")
    private String trialValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;
    private String trailingStopOwner;

}
