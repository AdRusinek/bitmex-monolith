package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
@Getter
@Setter
@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String alertMessage;
    @NotNull(message = "You have to provide triggering price")
    @Positive(message = "This has to be positive value")
    private Double alertTriggeringPrice;
    private String alertOwner;
    private String direction;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}

