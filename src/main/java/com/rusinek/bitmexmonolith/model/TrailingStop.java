package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Adrian Rusinek on 24.02.2020
 **/
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setTrialValue(String trialValue) {
        this.trialValue = trialValue;
    }

    public String getTrailingStopOwner() {
        return trailingStopOwner;
    }

    public void setTrailingStopOwner(String trailingStopOwner) {
        this.trailingStopOwner = trailingStopOwner;
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


    public Double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTrialValue() {
        return Double.valueOf(trialValue);
    }

    public void setTrialValue(Double trialValue) {
        this.trialValue = String.valueOf(trialValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrailingStop that = (TrailingStop) o;

        if (!startingPrice.equals(that.startingPrice)) return false;
        if (!quantity.equals(that.quantity)) return false;
        if (!trialValue.equals(that.trialValue)) return false;
        return user.equals(that.user);

    }

    @Override
    public int hashCode() {
        int result = startingPrice.hashCode();
        result = 31 * result + quantity.hashCode();
        result = 31 * result + trialValue.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TrailingStop{" +
                "id=" + id +
                ", startingPrice=" + startingPrice +
                ", quantity=" + quantity +
                ", trialValue=" + trialValue +
                '}';
    }
}
