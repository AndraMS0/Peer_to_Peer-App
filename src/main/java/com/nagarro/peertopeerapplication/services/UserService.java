package com.nagarro.peertopeerapplication.services;

import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.model.Account;
import com.nagarro.peertopeerapplication.model.Transaction;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.TransactionRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, TransactionRepository transactionRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }


    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword());
    }


    public UserDTO registerUser(String username, String plainTextPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User already exists with this username.");
        }
        String hashedPassword = passwordEncoder.encode(plainTextPassword);
        User newUser = new User(username, hashedPassword);
        newUser = userRepository.save(newUser);
        return convertToDTO(newUser);
    }

    public UserDTO logIn(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return convertToDTO(user);
        }
        throw new IllegalArgumentException("Invalid username or password.");
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUser_Id(userId);
    }
    public boolean changePassword(Long userId, String newPassword){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }else{
            return false;
        }
    }

}
