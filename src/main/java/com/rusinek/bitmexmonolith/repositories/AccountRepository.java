package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Adrian Rusinek on 08.03.2020
 **/
public interface AccountRepository extends CrudRepository<Account, Long> {

    Iterable<Account> findAllByAccountOwner(String username);

    Account findByAccountOwnerAndId(String credentialsOwner, Long id);

    Optional<Account> getById(Long id);

}
