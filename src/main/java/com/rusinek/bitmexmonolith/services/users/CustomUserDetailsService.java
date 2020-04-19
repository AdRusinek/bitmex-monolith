package com.rusinek.bitmexmonolith.services.users;

import com.rusinek.bitmexmonolith.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
public interface CustomUserDetailsService extends UserDetailsService {

    User loadUserById(Long id);
}
