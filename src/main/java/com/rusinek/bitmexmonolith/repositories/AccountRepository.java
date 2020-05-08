package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Adrian Rusinek on 08.03.2020
 **/
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByAccountOwner(String username);

    Optional<Account> findByAccountOwnerAndId(String credentialsOwner, Long id);
}
