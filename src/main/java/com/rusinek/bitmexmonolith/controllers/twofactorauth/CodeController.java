package com.rusinek.bitmexmonolith.controllers.twofactorauth;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.security.Principal;

/**
 * Created by Adrian Rusinek on 13.05.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class CodeController {

    private final GoogleAuthenticator googleAuthenticator;

    @SneakyThrows
    @GetMapping("/generate")
    public Graphics2D generate(Principal principal) {

        final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials(principal.getName());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("BitMEX App", principal.getName(), key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        return MatrixToImageWriter.toBufferedImage(bitMatrix).createGraphics();
    }

    @PostMapping("/validate/key")
    public Validation validateKey(Principal principal, @RequestBody ValidateCodeDto body) {
        return new Validation(googleAuthenticator.authorizeUser(principal.getName(), body.getCode()));
    }
}
