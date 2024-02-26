package com.nagarro.peertopeerapplication.validators;

import com.nagarro.peertopeerapplication.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator {

    private static final Pattern  PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$");

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required", "Username is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required", "Password is required.");
        if (user.getUsername().length() < 3 || user.getUsername().length() > 50) {
            errors.rejectValue("username", "user.username.size", "Username must be between 3 and 50 characters long.");
        }

        if(!PASSWORD_PATTERN.matcher(user.getPassword()).matches()){
            errors.rejectValue("password", "user.password.pattern", "Password must contain at least one lowercase letter, one uppercase letter, one number and to have at least 6 characters length.");
        }
    }
}
