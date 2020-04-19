package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Adrian Rusinek on 01.03.2020
 **/
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User getById(Long id);
}
