package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.model.Account;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

     @Mock
    private AccountRepository accountRepository;

     @InjectMocks
    private AccountService accountService;

     @BeforeEach
    public void setUp(){
         MockitoAnnotations.openMocks(this);
     }

     @Test
     public void createAccountTest(){
      Long userId = 1L;
      String currency = "RON";

      when(userRepository.findById(userId)).thenReturn(Optional.of(new User("username1", "Password11")));
      when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

      Account account = this.accountService.createAccount(userId, currency);

      assertEquals(account.getUser().getUsername(), "username1" );
      assertEquals(account.getUser().getPassword(), "Password11");
      assertEquals(account.getOwnerId(), userId);
      verify(userRepository, times(1)).findById(userId);
      verify(accountRepository, times(1)).save(any(Account.class));
     }

     @Test
     public void getAccountsByOwnerIdTest(){
        Long userId = 2L;
        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(account1);
        mockAccounts.add(account2);

        when(accountRepository.findByOwnerId(userId)).thenReturn(mockAccounts);

        List<Account> accounts = accountService.getAccountsByOwnerId(userId);

        assertEquals(accounts.size(), 2);
        verify(accountRepository, times(1)).findByOwnerId(userId);
     }

    @Test
    public void getAccountsByCurrencyTest(){
        String currency = "EUR";
        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(account1);
        mockAccounts.add(account2);
        mockAccounts.add(account3);

        when(accountRepository.findByCurrency(currency)).thenReturn(mockAccounts);

        List<Account> accounts = accountService.getAccountsByCurrency(currency);

        assertEquals(accounts.size(), 3);
        verify(accountRepository, times(1)).findByCurrency(currency);
    }

    @Test
    public void depositTest(){
         String accountId = "IDAc11";
         float amount1 = 300.5f;
        float amount2 = 10f;
         Account account = new Account();
         when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

         assertEquals(account.getBalance(),  0f);
         accountService.deposit(accountId, amount1);
         assertEquals(account.getBalance(),  300.5f);

        accountService.deposit(accountId, amount2);
        assertEquals(account.getBalance(),  310.5f);

        verify(accountRepository, times(2)).findById(accountId);
    }

    @Test
    void withdraw_EnoughBalance_AccountUpdatedTest() {

        String accountId = "account1";
        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        mockAccount.setBalance(200.0f);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        accountService.withdraw(accountId, 100.0f);

        assertEquals(100.0f, mockAccount.getBalance());
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void getBalanceTest() {
        String accountId = "account3";
        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        mockAccount.setBalance(150.0f);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        float balance = accountService.getBalance(accountId);

        assertEquals(150.0f, balance);
    }

    @Test
    void checkAccountStatus_LowBalanceTest() {

        String accountId = "account4";
        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        mockAccount.setBalance(50.0f);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        String status = accountService.checkAccountStatus(accountId);

        assertEquals("Low Balance", status);
    }


}
