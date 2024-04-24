package com.nagarro.peertopeerapplication.services;

import com.nagarro.peertopeerapplication.dto.AccountDTO;
import com.nagarro.peertopeerapplication.model.Account;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public synchronized AccountDTO createAccount(Long userId, String currency) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Account account = new Account();
        account.setCurrency(currency);
        account.setOwnerId(userId);
        account.setUser(user);
        accountRepository.save(account);
        return convertToDTO(account);
    }

    private AccountDTO convertToDTO(Account account) {
        return new AccountDTO(account.getId(), account.getBalance(), account.getCurrency(), account.getOwnerId());
    }

    public List<AccountDTO> getAccountsByOwnerId(Long ownerId) {
        List<Account> accounts = accountRepository.findByOwnerId(ownerId);
        return accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AccountDTO> getAccountsByCurrency(String currency) {
        List<Account> accounts = accountRepository.findByCurrency(currency);
        return accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deposit(Long accountId, BigInteger amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    public void withdraw(Long accountId, BigInteger amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (account.getBalance().subtract(amount).compareTo(BigInteger.ZERO) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
        }
    }

    public BigInteger getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        return account.getBalance();
    }

    public String checkAccountStatus(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        BigInteger balance = account.getBalance();
        return balance.compareTo(BigInteger.valueOf(100)) < 0 ? "Low Balance" : "Balance is sufficient";
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
