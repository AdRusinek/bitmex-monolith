package com.rusinek.bitmexmonolith.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by Adrian Rusinek on 28.03.2020
 **/
@Service
@RequiredArgsConstructor
public class MailService {


    private final JavaMailSender javaMailSender;

    public void sendMail(String to,
                         String subject,
                         String text,
                         String from,
                         boolean isHtmlContent,
                         File qrImage) throws MessagingException {


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessage.setFrom(from);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, isHtmlContent);
        if (qrImage != null) {
            mimeMessageHelper.addAttachment("TwoFactorAuth.jpg", qrImage);
        }
        javaMailSender.send(mimeMessage);

    }
}

