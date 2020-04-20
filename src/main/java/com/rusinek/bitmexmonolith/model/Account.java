package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    @JsonIgnore
    private List<TrailingStop> trailingStops = new ArrayList<>();

}
