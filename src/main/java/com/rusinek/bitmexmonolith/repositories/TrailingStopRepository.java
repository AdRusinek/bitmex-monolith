package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.TrailingStop;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Adrian Rusinek on 25.02.2020
 **/
public interface TrailingStopRepository extends CrudRepository<TrailingStop, Long> {

    Iterable<TrailingStop> findAllByTrailingStopOwner(String username);
}
