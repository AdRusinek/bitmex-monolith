package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Adrian Rusinek on 28.03.2020
 **/
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByValue(String value);
}
