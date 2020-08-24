package com.rusinek.bitmexmonolith.repositories.security;

import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.model.security.LoginFailure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Adrian Rusinek on 24.08.2020
 **/
public interface LoginFailureRepository extends JpaRepository<LoginFailure, Long> {

    List<LoginFailure> findAllByUserAndCreatedDateIsAfter(User user, Timestamp timestamp);
}
