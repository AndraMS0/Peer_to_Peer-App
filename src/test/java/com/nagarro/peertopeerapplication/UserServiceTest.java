package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.model.Transaction;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.TransactionRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;


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
       String username = "testUser";
       String password = "testPassword";
       User mockUser = new User();
       mockUser.setUsername("testUser");
       mockUser.setPassword(passwordEncoder.encode("testPassword"));

       when(userRepository.findByUsername(username)).thenReturn(mockUser);
       when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);

       UserDTO userDTO = userService.logIn(username, password);

       assertEquals(username, userDTO.getUsername());
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
