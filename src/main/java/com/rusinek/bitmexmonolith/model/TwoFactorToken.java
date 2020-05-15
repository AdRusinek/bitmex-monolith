package com.rusinek.bitmexmonolith.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Adrian Rusinek on 13.05.2020
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class TwoFactorToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String secretKey;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;
}
