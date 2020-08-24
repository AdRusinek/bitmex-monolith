package com.rusinek.bitmexmonolith.repositories.security;

import com.rusinek.bitmexmonolith.model.security.LoginSuccess;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Adrian Rusinek on 24.08.2020
 **/
public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, Long> {
}
