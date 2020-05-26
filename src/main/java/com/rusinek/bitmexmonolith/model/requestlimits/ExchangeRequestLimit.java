package com.rusinek.bitmexmonolith.model.requestlimits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Adrian Rusinek on 26.05.2020
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ExchangeRequestLimit extends RequestLimit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
