package com.rusinek.bitmexmonolith.services.users;

import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.payload.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by Adrian Rusinek on 01.03.2020
 **/
public interface UserService {

    User saveUser(User user);

    RedirectView verifyToken(String value);

    ResponseEntity<?> authenticateUser(LoginRequest request, BindingResult result);

    ResponseEntity<?> registerUser(User user, BindingResult result);
}
