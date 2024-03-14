package com.nagarro.peertopeerapplication.services;


//<<<<<<< Updated upstream

import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.model.Account;
import com.nagarro.peertopeerapplication.model.Transaction;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.TransactionRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
//=======
//>>>>>>> Stashed changes
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;


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
        String encodedPassword = passwordEncoder.encode(password);
        if (user != null && passwordEncoder.matches(encodedPassword, user.getPassword())) {
            return convertToDTO(user);
        }
        throw new IllegalArgumentException("Invalid username or password.");
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUser_Id(userId);
    }

    public BigInteger calculateTotalBalance(Long userId) {
        List<Account> accounts = accountRepository.findByOwnerId(userId);
        BigInteger totalBalance = BigInteger.ZERO;
        for (Account account : accounts) {
            totalBalance = totalBalance.add(account.getBalance());
        }
        return totalBalance;
    }

}
