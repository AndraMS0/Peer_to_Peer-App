package com.nagarro.peertopeerapplication.controller;

import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping()
    public User registerUser(@RequestBody User user) {
        user = service.registerUser(user.getUsername(), user.getPassword());
        return user;
    }

    @PostMapping("/login")
    public ResponseEntity<User> logIn(@RequestBody User user) {
        User loggedInUser = service.logIn(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(loggedInUser);
    }

    @GetMapping("/{userId}/total-balance")
    public ResponseEntity<BigInteger> getTotalBalance(@PathVariable Long userId) {
        BigInteger totalBalance = service.calculateTotalBalance(userId);
        return new ResponseEntity<>(totalBalance, HttpStatus.OK);
    }


}
