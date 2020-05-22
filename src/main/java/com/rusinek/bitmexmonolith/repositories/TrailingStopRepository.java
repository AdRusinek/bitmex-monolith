package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.Account;
import com.rusinek.bitmexmonolith.model.TrailingStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Adrian Rusinek on 25.02.2020
 **/
public interface TrailingStopRepository extends JpaRepository<TrailingStop, Long> {

    Iterable<TrailingStop> findAllByTrailingStopOwner(String username);

    void deleteById(Long id);
}
