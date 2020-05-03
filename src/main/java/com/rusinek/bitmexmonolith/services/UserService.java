package com.rusinek.bitmexmonolith.services;

import com.rusinek.bitmexmonolith.exceptions.MapValidationErrorService;
import com.rusinek.bitmexmonolith.exceptions.authenticationException.UsernameAlreadyExistsException;
import com.rusinek.bitmexmonolith.model.Token;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.payload.JWTLoginSuccessResponse;
import com.rusinek.bitmexmonolith.payload.LoginRequest;
import com.rusinek.bitmexmonolith.repositories.TokenRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.security.JwtTokenProvider;
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

import javax.mail.MessagingException;

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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MapValidationErrorService errorService;
    private final UserValidator userValidator;
    private final MailService mailService;
    private final TokenRepository tokenRepository;
    @Value("${bitmex-monolith.token-redirection-url}")
    private String tokenRedirectionUrl;
    @Value("${bitmex-monolith.default-url}")
    private String defaultUrl;

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

    public ResponseEntity<?> authenticateUser(LoginRequest request, BindingResult result) {

        if(result.hasErrors()) return errorService.validateErrors(result);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
    }

    public ResponseEntity<?> registerUser(User user, BindingResult result) {
        // validate passwords match
        userValidator.validate(user, result);

        if(result.hasErrors()) return errorService.validateErrors(result);

        User newUser = saveUser(user);
        sendToken(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    public RedirectView verifyToken(String value) {
        Token token = tokenRepository.findByValue(value);
        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return new RedirectView(tokenRedirectionUrl + "/login");
    }

    private void sendToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setUser(user);
        tokenRepository.save(token);

        String url = defaultUrl + "/api/users/token?value=" + tokenValue;

        try {
            log.info("Sending registration email to user: " + user.getUsername());
            mailService.sendMail(user.getUsername(), "Confirm account", url, "bitmexprogram@gmail.com", false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
