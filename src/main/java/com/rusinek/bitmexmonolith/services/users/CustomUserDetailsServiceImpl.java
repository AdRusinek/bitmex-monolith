package com.rusinek.bitmexmonolith.services.users;

import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User not found.");
        return user;
    }

    @Override
    @Transactional
    public User loadUserById(Long id) {
        User user = userRepository.getById(id);
        if (user == null) throw new UsernameNotFoundException("User not found.");
        return user;
    }
}
