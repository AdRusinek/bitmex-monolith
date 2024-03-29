package com.rusinek.bitmexmonolith.model.limits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rusinek.bitmexmonolith.model.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Adrian Rusinek on 08.05.2020
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class AccountRequestLimit extends RequestLimit {

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Account account;
}
