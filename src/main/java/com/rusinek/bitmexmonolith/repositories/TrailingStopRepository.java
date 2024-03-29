package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.TrailingStop;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
public interface TrailingStopRepository extends JpaRepository<TrailingStop, Long> {

    Iterable<TrailingStop> findAllByStopOwner(String username);
}
