package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.Token;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Adrian Rusinek on 28.03.2020
 **/
public interface TokenRepository extends CrudRepository<Token, Long> {

    Token findByValue(String value);
}
