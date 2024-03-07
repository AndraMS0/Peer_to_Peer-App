package com.nagarro.peertopeerapplication.services;


//<<<<<<< Updated upstream

import com.nagarro.peertopeerapplication.model.Account;
import com.nagarro.peertopeerapplication.model.Transaction;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.GenericUserRepository;
import com.nagarro.peertopeerapplication.repositories.TransactionRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
//=======
import com.nagarro.peertopeerapplication.model.Account;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.model.Transaction;
import com.nagarro.peertopeerapplication.repositories.GenericUserRepository;
import com.nagarro.peertopeerapplication.repositories.TransactionRepository;
//>>>>>>> Stashed changes
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    private final GenericUserRepository genericUserRepository;


    @Autowired
    public UserService(UserRepository userRepository, AccountService accountService, TransactionRepository transactionRepository, GenericUserRepository genericUserRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.genericUserRepository = genericUserRepository;
        this.accountRepository = accountRepository;

    }

    public User getUserByUsername(String username) {
        return genericUserRepository.findByUsername(username);
    }


    public User registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User already exists with this username.");
        }
        User newUser = new User(username, password);
        return userRepository.save(newUser);
    }

    public User logIn(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
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
