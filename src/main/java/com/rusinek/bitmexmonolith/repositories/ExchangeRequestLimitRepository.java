package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.requestlimits.ExchangeRequestLimit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Adrian Rusinek on 26.05.2020
 **/
public interface ExchangeRequestLimitRepository extends JpaRepository<ExchangeRequestLimit, Long> {

}
