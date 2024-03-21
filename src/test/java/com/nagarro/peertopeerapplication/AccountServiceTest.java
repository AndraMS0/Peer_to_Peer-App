package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.dto.AccountDTO;
import com.nagarro.peertopeerapplication.model.Account;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;


    @Test
    public void createAccountTest() {
        Long userId = 1L;
        String currency = "USD";
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Account account = new Account();
        account.setCurrency(currency);
        account.setOwnerId(userId);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO result = accountService.createAccount(userId, currency);

        assertNotNull(result);
        assertEquals(currency, result.getCurrency());
        assertEquals(userId, result.getOwnerId());
    }

    @Test
    public void getAccountsByOwnerIdTest() {
        Long userId = 2L;
        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(account1);
        mockAccounts.add(account2);

        when(accountRepository.findByOwnerId(userId)).thenReturn(mockAccounts);

        List<AccountDTO> accounts = accountService.getAccountsByOwnerId(userId);

        assertEquals(accounts.size(), 2);
        verify(accountRepository, times(1)).findByOwnerId(userId);
    }

    @Test
    public void getAccountsByCurrencyTest() {
        String currency = "EUR";
        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(account1);
        mockAccounts.add(account2);
        mockAccounts.add(account3);

        when(accountRepository.findByCurrency(currency)).thenReturn(mockAccounts);

        List<AccountDTO> accounts = accountService.getAccountsByCurrency(currency);

        assertEquals(accounts.size(), 3);
        verify(accountRepository, times(1)).findByCurrency(currency);
    }

    @Test
    public void depositTest() {
        Long accountId = 999L;
        BigInteger amount1 = BigInteger.valueOf(300);
        BigInteger amount2 = BigInteger.valueOf(10);
        Account account = new Account();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertEquals(account.getBalance(), BigInteger.valueOf(0));
        accountService.deposit(accountId, amount1);
        assertEquals(account.getBalance(), BigInteger.valueOf(300));

        accountService.deposit(accountId, amount2);
        assertEquals(account.getBalance(), BigInteger.valueOf(310));

        verify(accountRepository, times(2)).findById(accountId);
    }

    @Test
    void withdraw_EnoughBalance_AccountUpdatedTest() {
        Long accountId = 8888L;
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setBalance(BigInteger.valueOf(200));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        accountService.withdraw(accountId, BigInteger.valueOf(100));

        assertEquals(BigInteger.valueOf(100), mockAccount.getBalance());
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void getBalanceTest() {
        Long accountId = 888L;
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setBalance(BigInteger.valueOf(150));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        BigInteger balance = accountService.getBalance(accountId);

        assertEquals(BigInteger.valueOf(150), balance);
    }

    @Test
    void checkAccountStatus_LowBalanceTest() {
        Long accountId = 2222L;
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setBalance(BigInteger.valueOf(50));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        String status = accountService.checkAccountStatus(accountId);

        assertEquals("Low Balance", status);
    }


}
