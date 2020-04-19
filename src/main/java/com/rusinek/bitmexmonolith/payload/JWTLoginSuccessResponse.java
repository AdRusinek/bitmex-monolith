package com.rusinek.bitmexmonolith.payload;

import lombok.*;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JWTLoginSuccessResponse {

    private boolean success;
    private String token;
}
