package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.model.TwoFactorToken;
import com.warrenstrange.googleauth.ICredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by Adrian Rusinek on 13.05.2020
 **/
@Component
@RequiredArgsConstructor
public class CredentialRepository implements ICredentialRepository {

    private final UserRepository userRepository;

    @Override
    public String getSecretKey(String userName) {

        Optional<User> user = userRepository.findByUsername(userName);

        return user.map(value -> value.getTwoFactorToken().getSecretKey()).orElse(null);
    }

    @Override
    public void saveUserCredentials(String userName,
                                    String secretKey,
                                    int validationCode,
                                    List<Integer> scratchCodes) {

        Optional<User> user = userRepository.findByUsername(userName);

        if (user.isPresent()) {
            user.get().setTwoFactorToken(new TwoFactorToken());
            user.get().getTwoFactorToken().setUsername(userName);
            user.get().getTwoFactorToken().setSecretKey(secretKey);
            user.get().getTwoFactorToken().setUser(user.get());
            userRepository.save(user.get());
        }
    }
}