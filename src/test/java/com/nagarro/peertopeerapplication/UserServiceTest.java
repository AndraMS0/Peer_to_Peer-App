package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.model.Transaction;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.TransactionRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private UserService userService;

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        String username = "newUser";
        String plainTextPassword = "Password11";
        String hashedPassword = "hashedPassword";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(plainTextPassword)).thenReturn(hashedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserDTO result = userService.registerUser(username, plainTextPassword);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).existsByUsername(username);
        verify(passwordEncoder, times(1)).encode(plainTextPassword);
    }

    @Test
    public void testRegisterUser_Fail() {
        String username = "existingUser";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(username, "Password22"));
    }

    @Test
    public void testLogIn_InvalidCredentials() {
        String username = "user222";
        String wrongPassword = "wrongPassword11";

        when(userRepository.findByUsername(username)).thenReturn((new User(username, "password1P")));

        assertThrows(IllegalArgumentException.class, () -> userService.logIn(username, wrongPassword));
    }

    @Test
    public void testLogIn_Success() {
        String username = "LogInUser";
        String password = "testPassword1";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedTestPassword");

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn("encodedTestPassword");
        when(passwordEncoder.matches("encodedTestPassword", "encodedTestPassword")).thenReturn(true);

        UserDTO userDTO = userService.logIn(username, password);
        assertEquals(username, userDTO.getUsername());
        assertEquals("encodedTestPassword", userDTO.getPassword());
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

}
