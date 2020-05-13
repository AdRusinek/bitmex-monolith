package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.RegisterToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Adrian Rusinek on 28.03.2020
 **/
public interface TokenRepository extends JpaRepository<RegisterToken, Long> {

    Optional<RegisterToken> findByValue(String value);
}
