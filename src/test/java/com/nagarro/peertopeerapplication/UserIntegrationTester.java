package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.model.Account;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTester {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeAll
    public static void init(){
       restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
       baseUrl = baseUrl.concat(":") .concat(port + "").concat("/users");
    }

    @Test
   // @Sql(statements = "INSERT INTO USERS (ID, SAVINGS_GROUP_ID, USERNAME, PASSWORD) VALUES (1, 2, 'testUser', 'Password22')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD )
    public void testRegisterUser(){
        User user = new User("username1", "Password11");
        User response = restTemplate.postForObject(baseUrl, user, User.class);
        assertEquals("username1", response.getUsername());
        assertEquals("Password11", response.getPassword());
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    public void testLoginSuccess(){
        User user = new User("username1", "Password111");
        restTemplate.postForObject(baseUrl, user, User.class);

        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl + "/login", user, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("username1", response.getBody().getUsername());

    }

    @Test
    public void testLoginFailure() {
        User invalidUser = new User("username2", "wrongPassword");
        try {
            restTemplate.postForEntity(baseUrl + "/login", invalidUser, User.class);
            fail("Should have thrown an HttpServerErrorException");
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }

    @Test
    public void testCalculateTotalBalance() {
        Long userId = 1L;
        ResponseEntity<BigInteger> response = restTemplate.getForEntity(
                 baseUrl + "/" + userId + "/total-balance",
                BigInteger.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Account> accounts = accountRepository.findByOwnerId(userId);
        BigInteger expectedTotalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigInteger.ZERO, BigInteger::add);

        assertEquals(expectedTotalBalance, response.getBody());
    }

    }


