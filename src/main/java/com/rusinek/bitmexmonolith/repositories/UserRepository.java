package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Created by Adrian Rusinek on 01.03.2020
 **/
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> getById(Long id);

    List<User> findAllByAccountNonLockedAndLastModifiedDateIsBefore(boolean locked, Timestamp timestamp);
}
