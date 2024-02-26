package com.nagarro.peertopeerapplication.services;

import com.nagarro.peertopeerapplication.model.Account;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository){
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public synchronized Account createAccount(Long userId, String currency){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Account account = new Account();
        account.setCurrency(currency);
        account.setOwnerId(userId);
        account.setUser(user);
        accountRepository.save(account);
        return account;
    }

    public List<Account> getAccountsByOwnerId(Long ownerId){
        return this.accountRepository.findByOwnerId(ownerId);
    }

    public List<Account> getAccountsByCurrency(String currency){
        return this.accountRepository.findByCurrency(currency);
    }

    public void deposit(String accountId, float amount){
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    public void withdraw(String accountId, float amount){
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if(account.getBalance() - amount >= 0){
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
        }
    }

    public float getBalance(String accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        return account.getBalance();
    }

    public String checkAccountStatus(String accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        float balance = account.getBalance();
        return balance < 100.0 ? "Low Balance" : "Balance is sufficient";
    }

}
