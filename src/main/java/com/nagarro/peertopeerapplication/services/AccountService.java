package com.nagarro.peertopeerapplication.services;

import com.nagarro.peertopeerapplication.model.Account;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private static int accountId = 0;
    private final Map<String, Account> accounts = new HashMap<>();

    public synchronized void createAccount(String userId, String currency) {
        Account account = new Account();
        account.setAccountId(String.valueOf(accountId));
        account.setCurrency(currency);
        account.setOwnerId(userId);
        accounts.put(String.valueOf(accountId), account);
    }

    public synchronized void deposit(String accountId, float amount) {
        Account account = accounts.get(accountId);
        account.setBalance(account.getBalance() + amount);
    }

    public synchronized void withdraw(String accountId, float amount) {
        Account account = accounts.get(accountId);
        account.setBalance(account.getBalance() - amount);
    }

    public synchronized float getBalance(String accountId) {
        Account account = accounts.get(accountId);
        return account.getBalance();
    }
}
