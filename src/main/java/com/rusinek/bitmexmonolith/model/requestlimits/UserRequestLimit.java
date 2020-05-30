package com.rusinek.bitmexmonolith.model.requestlimits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rusinek.bitmexmonolith.model.User;
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
public class UserRequestLimit extends RequestLimit {

    private int connectionTestLimit;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;
}
