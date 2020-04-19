package com.rusinek.bitmexmonolith.services.users;

import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsException;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.payload.JWTLoginSuccessResponse;
import com.rusinek.bitmexmonolith.payload.LoginRequest;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.security.JwtTokenProvider;
import com.rusinek.bitmexmonolith.services.token.TokenService;
import com.rusinek.bitmexmonolith.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import static com.rusinek.bitmexmonolith.security.SecurityConstants.TOKEN_PREFIX;


/**
 * Created by Adrian Rusinek on 01.03.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MapValidationErrorService errorService;
    private final UserValidator userValidator;
    private final TokenService tokenService;

    @Override
    public User saveUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setConfirmPassword("");
            return userRepository.save(user);
        } catch (Exception e) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' already exists.");
        }

        //username has to be unique(Exception)

        //make sure that password and confirmPassword match

        //dont persist or show confirm password
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest request, BindingResult result) {
        ResponseEntity<?> errorMap = errorService.validateErrors(result);
        if (errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
    }

    @Override
    public ResponseEntity<?> registerUser(User user, BindingResult result) {
        // validate passwords match
        userValidator.validate(user, result);

        ResponseEntity<?> errorMap = errorService.validateErrors(result);
        if (errorMap != null) return errorMap;

        User newUser = saveUser(user);
        tokenService.sendToken(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
