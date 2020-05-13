package com.rusinek.bitmexmonolith.controllers.twofactorauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Adrian Rusinek on 13.05.2020
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
class ValidateCodeDto {

    private Integer code;
}
