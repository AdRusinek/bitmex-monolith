package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.StopMarket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Adrian Rusinek on 03.06.2020
 **/
public interface StopMarketRepository extends JpaRepository<StopMarket, Long> {

    Iterable<StopMarket> findAllByStopOwner(String username);
}
