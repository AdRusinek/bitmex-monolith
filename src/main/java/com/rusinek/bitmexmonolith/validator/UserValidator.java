package com.rusinek.bitmexmonolith.validator;

import com.rusinek.bitmexmonolith.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Adrian Rusinek on 02.03.2020
 **/
@Component
public class UserValidator implements Validator {

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getPassword().length() < 6) {
            errors.rejectValue("password", "Length","Password must be at least 6 characters.");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword","Match","Password must match.");
        }
    }
}
