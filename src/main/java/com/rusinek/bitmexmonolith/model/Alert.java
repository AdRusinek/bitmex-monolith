package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;
import java.util.Date;

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

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;
    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
}

