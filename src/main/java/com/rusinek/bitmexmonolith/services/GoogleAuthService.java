package com.rusinek.bitmexmonolith.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rusinek.bitmexmonolith.model.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Adrian Rusinek on 04.06.2020
 **/
@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleAuthenticator googleAuthenticator;
    @Value("${bitmex-monolith.temporary-qrs}")
    private String temporaryQrStorage;

    @SneakyThrows
    File generateQr(User user) {

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


}
