package com.rusinek.bitmexmonolith.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @OneToOne
    private User user;

}
