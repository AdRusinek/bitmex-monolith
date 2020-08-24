package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsException;
import com.rusinek.bitmexmonolith.model.RegisterToken;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.TokenRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.security.JWTLoginSuccessResponse;
import com.rusinek.bitmexmonolith.security.JwtTokenProvider;
import com.rusinek.bitmexmonolith.security.LoginRequest;
import com.rusinek.bitmexmonolith.validator.CodeValidator;
import com.rusinek.bitmexmonolith.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import static com.rusinek.bitmexmonolith.security.SecurityConstants.TOKEN_PREFIX;


/**
 * Created by Adrian Rusinek on 01.03.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GoogleAuthService googleAuthService;
    private final LimitService limitService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MapValidationErrorService errorService;
    private final UserValidator userValidator;
    private final CodeValidator codeValidator;
    private final MailService mailService;
    private final TokenRepository tokenRepository;
    @Value("${bitmex-monolith.registerToken-redirection-url}")
    private String tokenRedirectionUrl;
    @Value("${bitmex-monolith.default-url}")
    private String defaultUrl;


    private User saveUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setConfirmPassword("");

            User savedUser = userRepository.save(user);
            limitService.saveUserRequestLimit(savedUser);

            return savedUser;

        } catch (Exception e) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' already exists.");
        }
    }

    public ResponseEntity<?> registerUser(User user, BindingResult result) {


        // processErrorResponse passwords match
        userValidator.validate(user, result);


        if (result.hasErrors()) {
            return errorService.validateErrors(result);
        }

        User newUser = saveUser(user);
        sendRegistryTokenAndQrImage(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    public ResponseEntity<?> authenticateUser(LoginRequest request, BindingResult result) {

        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if (user.isPresent()) {
//            codeValidator.validate(request, result);
        }

        if (result.hasErrors()) {
            return errorService.validateErrors(result);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
    }


    public RedirectView verifyToken(String value) {
        tokenRepository.findByValue(value).ifPresent(registerToken -> {
            User user = registerToken.getUser();
            user.setTokenVerified(true);
            userRepository.save(user);
            log.debug("Validating verify token for user " + user.getUsername());
        });
        return new RedirectView(tokenRedirectionUrl + "/");
    }

    private void sendRegistryTokenAndQrImage(User user) {

        File qrImage = googleAuthService.generateQr(user);
        String url = defaultUrl + "/api/users/token?value=" + generateRegistryToken(user);

        try {
            log.debug("Sending registration email to user: " + user.getUsername());
            mailService.sendMail(user.getUsername(), "Confirm account", url,
                    "bitmexprogram@gmail.com", false, qrImage);
            if (!qrImage.delete()) {
                log.error("Couldn't delete qr image");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private String generateRegistryToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        RegisterToken registerToken = new RegisterToken();
        registerToken.setValue(tokenValue);
        registerToken.setUser(user);
        tokenRepository.save(registerToken);

        return tokenValue;
    }

}
