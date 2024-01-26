package com.nagarro.peertopeerapplication.services;

import com.nagarro.peertopeerapplication.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();
    private final AccountService accountService;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public User registerUser(String userId, String username, String password) {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("User already exists with this ID.");
        }
        User newUser = new User(userId, username, password);
        users.put(userId, newUser);
        return newUser;
    }

    public User logIn(String username, String password) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new IllegalArgumentException("Invalid username or password.");
    }

    public String getUsername(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        return user.getUsername();
    }

    public void joinGroup(String userId, String groupId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        //will be added with GroupService
    }
}
