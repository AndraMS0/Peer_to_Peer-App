package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.model.Transaction;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.GenericUserRepository;
import com.nagarro.peertopeerapplication.repositories.TransactionRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenericUserRepository genericUserRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        String username = "newUser";
        String password = "Password11";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.registerUser(username, password);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    public void testRegisterUser_Fail() {
        String username = "existingUser";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(username, "Password22");
        });
    }

    @Test
    public void testLogIn_InvalidCredentials() {
        String username = "user222";
        String wrongPassword = "wrongPassword11";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, "password1P")));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.logIn(username, wrongPassword);
        });

    }

    @Test
    public void testGetTransactionsByUserid() {
        Long userId = 1L;
        List<Transaction> mockTransactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        mockTransactions.add(transaction1);
        mockTransactions.add(transaction2);

        when(transactionRepository.findByUser_Id(userId)).thenReturn(mockTransactions);

        List<Transaction> transactions = userService.getTransactionsByUserId(userId);

        assertNotNull(transactions, "Transactions should not be null");
        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findByUser_Id(userId);
    }

    @Test
    public void testGetUserByUsernameGenericRepositoryTest() {

        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);
        when(genericUserRepository.findByUsername(username)).thenReturn(mockUser);

        User result = userService.getUserByUsername(username);

        assertEquals(username, result.getUsername());
        verify(genericUserRepository).findByUsername(username);
    }


}
