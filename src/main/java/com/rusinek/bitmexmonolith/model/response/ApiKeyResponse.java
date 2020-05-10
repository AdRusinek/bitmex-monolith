package com.rusinek.bitmexmonolith.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian Rusinek on 10.05.2020
 **/
@Getter
@Setter
@ToString
public class ApiKeyResponse {

    private String id;
    private String name;
    private Integer nonce;
    private String cidr;
    private List<String> permissions = new ArrayList<>();
    private Boolean enabled;
    private Integer userId;
    private Timestamp created;

}