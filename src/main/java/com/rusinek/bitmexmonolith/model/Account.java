package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rusinek.bitmexmonolith.model.requestlimits.AccountRequestLimit;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
    @NotBlank(message = "Account name is required")
    private String accountName;
    @NotBlank(message = "Api key must not be blank")
    private String apiKey;
    @NotBlank(message = "Secret must not be blank")
    private String apiKeySecret;
    private String accountOwner;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;
    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TrailingStop> trailingStops = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")
    @JsonIgnore
    private AccountRequestLimit accountRequestLimit;

}
