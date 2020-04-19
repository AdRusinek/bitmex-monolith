package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "What you want to see being sent to your mail.")
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAlertOwner() {
        return alertOwner;
    }

    public void setAlertOwner(String alertOwner) {
        this.alertOwner = alertOwner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }


    public Double getAlertTriggeringPrice() {
        return alertTriggeringPrice;
    }

    public void setAlertTriggeringPrice(Double alertTriggeringPrice) {
        this.alertTriggeringPrice = alertTriggeringPrice;
    }
}

