package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by Adrian Rusinek on 24.02.2020
 **/
@Getter
@Setter
@Entity
public class TrailingStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Starting price is required.")
    private Double startingPrice;
    @NotNull(message = "Quantity is required.")
    private Double quantity;
    @NotEmpty(message = "Trial Value is required.")
    private String trialValue;
    private String execInst;
    private Boolean closeOnTrigger;
    private String trailingStopOwner;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    @JsonIgnore
    private Account account;

}
