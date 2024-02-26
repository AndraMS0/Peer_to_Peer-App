package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidatorTest {

    private UserValidator validator;
    private User user;
    private Errors errors;

    @BeforeEach
    public void setup(){
        validator = new UserValidator();
        user = new User();
        errors = new BeanPropertyBindingResult(user, "user");
    }

    @Test
    public void shouldRejectEmptyUsername(){
        user.setUsername("");
        user.setPassword("Password1");

        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("username"));
        assertTrue(errors.getFieldError("username").getCode().contains("field.required"));
    }

    @Test
    public void shouldRejectShortUsername() {
        user.setUsername("ab");
        user.setPassword("Password1");

        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("username"));
        assertTrue(errors.getFieldError("username").getCode().contains("user.username.size"));
    }

    @Test
    public void shouldRejectEmptyPassword() {
        user.setUsername("validUsername");
        user.setPassword("");

        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("password"));
        assertTrue(errors.getFieldError("password").getCode().contains("field.required"));
    }

    @Test
    public void shouldRejectInvalidPassword() {
        user.setUsername("validUsername");
        user.setPassword("pass");

        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("password"));
        assertTrue(errors.getFieldError("password").getCode().contains("user.password.pattern"));
    }



}
