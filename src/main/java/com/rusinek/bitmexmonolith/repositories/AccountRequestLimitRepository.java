package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.limits.AccountRequestLimit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Adrian Rusinek on 08.05.2020
 **/
public interface AccountRequestLimitRepository extends JpaRepository<AccountRequestLimit, Long> {
}
