package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian Rusinek on 21.02.2020
 **/
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String credentialsName;
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

    public List<TrailingStop> getTrailingStops() {
        return trailingStops;
    }

    public void setTrailingStops(List<TrailingStop> trailingStops) {
        this.trailingStops = trailingStops;
    }

    public String getCredentialsName() {
        return credentialsName;
    }

    public void setCredentialsName(String credentialsName) {
        this.credentialsName = credentialsName;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKeySecret() {
        return apiKeySecret;
    }

    public void setApiKeySecret(String apiKeySecret) {
        this.apiKeySecret = apiKeySecret;
    }
}
