package com.rusinek.bitmexmonolith.controllers;

import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.payload.LoginRequest;
import com.rusinek.bitmexmonolith.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

/**
 * Created by Adrian Rusinek on 01.03.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              BindingResult result) {
        return userService.authenticateUser(loginRequest, result);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user,
                                          BindingResult result) {
        return userService.registerUser(user, result);
    }

    @GetMapping("/token")
    public RedirectView verifyToken(@RequestParam String value) {
        return userService.verifyToken(value);
    }
}
