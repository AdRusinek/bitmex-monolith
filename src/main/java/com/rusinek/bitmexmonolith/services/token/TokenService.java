package com.rusinek.bitmexmonolith.services.token;


import com.rusinek.bitmexmonolith.model.Token;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.TokenRepository;
import com.rusinek.bitmexmonolith.repositories.UserRepository;
import com.rusinek.bitmexmonolith.services.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import java.util.UUID;

/**
 * Created by Adrian Rusinek on 28.03.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final MailService mailService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public RedirectView verifyToken(String value) {
        Token token = tokenRepository.findByValue(value);
        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return new RedirectView("http://localhost:3000/login");
    }

    public void sendToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setUser(user);
        tokenRepository.save(token);

        //todo trzeba zmienic jakby sie na serwer wrzucalo
        String url = "http://localhost:8080/api/users/token?value=" + tokenValue;

        try {
            log.info("Sending registration email to user: " + user.getUsername());
            mailService.sendMail(user.getUsername(), "Confirm account", url, "bitmexprogram@gmail.com", false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
