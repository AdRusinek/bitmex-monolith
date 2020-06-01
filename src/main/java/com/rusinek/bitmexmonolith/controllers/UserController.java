package com.rusinek.bitmexmonolith.controllers;

import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.model.limits.IpAddressRequestLimit;
import com.rusinek.bitmexmonolith.repositories.IpAddressRequestLimitRepository;
import com.rusinek.bitmexmonolith.security.LoginRequest;
import com.rusinek.bitmexmonolith.services.IpAddressService;
import com.rusinek.bitmexmonolith.services.UserService;
import com.rusinek.bitmexmonolith.util.HttpReqRespUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by Adrian Rusinek on 01.03.2020
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final HttpReqRespUtils requestUtils;
    private final IpAddressService ipAddressService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              BindingResult result) {

        String ip = requestUtils.getClientIpAddressIfServletRequestExist();

        boolean b = ipAddressService.areIpRequestsOverloaded(ip);

        System.out.println(b);

        return userService.authenticateUser(loginRequest, result, ip,b );
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
