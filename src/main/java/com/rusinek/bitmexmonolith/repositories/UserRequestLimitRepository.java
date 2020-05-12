package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.requestlimits.UserRequestLimit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Adrian Rusinek on 08.05.2020
 **/
public interface UserRequestLimitRepository extends JpaRepository<UserRequestLimit,Long> {
}