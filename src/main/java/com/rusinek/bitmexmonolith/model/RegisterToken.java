package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Adrian Rusinek on 28.03.2020
 **/
@Getter
@Setter
@Entity
public class RegisterToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

}
