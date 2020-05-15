package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rusinek.bitmexmonolith.model.requestlimits.AccountRequestLimit;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian Rusinek on 21.02.2020
 **/
@Getter
@Setter
@Entity
public class Account {

    //todo [1] zabezpieczenie account przed przekroczeniem limitu [2] hmac i dodatkowy algorytm przy wsadzaniu do bazy [3] castowanie odpowiedzi od mexa

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Account name is required")
    private String accountName;
    @NotBlank(message = "Api key must not be blank")
    private String apiKey;
    @NotBlank(message = "Secret must not be blank")
    private String apiKeySecret;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    private String accountOwner;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TrailingStop> trailingStops = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")
    @JsonIgnore
    private AccountRequestLimit accountRequestLimit;
}
