package com.rusinek.bitmexmonolith.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
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

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
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
    private final LimitService limitService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MapValidationErrorService errorService;
    private final UserValidator userValidator;
    private final MailService mailService;
    private final TokenRepository tokenRepository;
    private final GoogleAuthenticator googleAuthenticator;
    @Value("${bitmex-monolith.token-redirection-url}")
    private String tokenRedirectionUrl;
    @Value("${bitmex-monolith.default-url}")
    private String defaultUrl;
    @Value("${bitmex-monolith.temporary-qrs}")
    private String temporaryQrStorage;

    private User saveUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setConfirmPassword("");

            user.setUserRequestLimit(limitService.saveUserRequestLimit());
            return userRepository.save(user);
        } catch (Exception e) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' already exists.");
        }
    }

    public ResponseEntity<?> authenticateUser(LoginRequest request, BindingResult result) {

        if (result.hasErrors()) return errorService.validateErrors(result);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
    }

    @SneakyThrows
    private File sendCode(User user) {

        final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials(user.getUsername());

        //I've decided to generate QRCode on backend site
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("BitMEX app", user.getUsername(), key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        File outputFile = new File(temporaryQrStorage);
        ImageIO.write(bufferedImage, "jpg", outputFile);

        return outputFile;
    }

    public ResponseEntity<?> registerUser(User user, BindingResult result) {
        // processErrorResponse passwords match
        userValidator.validate(user, result);


        if (result.hasErrors()) return errorService.validateErrors(result);

        User newUser = saveUser(user);
        sendTokenAndQRCode(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    public RedirectView verifyToken(String value) {
        tokenRepository.findByValue(value).ifPresent(token -> {
            User user = token.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            log.debug("Validating verify token for user " + user.getUsername());
        });
        return new RedirectView(tokenRedirectionUrl + "/");
    }

    private void sendTokenAndQRCode(User user) {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setUser(user);
        tokenRepository.save(token);
        File qrFile = sendCode(user);
        String url = defaultUrl + "/api/users/token?value=" + tokenValue;

        try {
            log.debug("Sending registration email to user: " + user.getUsername());
            mailService.sendMail(user.getUsername(), "Confirm account", url,
                    "bitmexprogram@gmail.com", false, qrFile);
            if(!qrFile.delete()) {
             log.error("Couldn't delete qr image");
            }
        } catch (Exception e) {
            log.error("Error occurred while sending mail to " + user.getUsername());
        }
    }

    Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
