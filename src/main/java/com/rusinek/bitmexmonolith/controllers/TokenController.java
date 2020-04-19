package com.rusinek.bitmexmonolith.controllers;

import com.rusinek.bitmexmonolith.services.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by Adrian Rusinek on 28.03.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/token")
    public RedirectView verifyToken(@RequestParam String value) {
        return tokenService.verifyToken(value);
    }
}
