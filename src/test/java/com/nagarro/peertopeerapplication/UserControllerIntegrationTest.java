package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private RestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/v1/users");
        restTemplate = new RestTemplate();
    }

    @Test
    public void testRegisterUser() {
        UserDTO user = new UserDTO("username123", "Password1130");
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(baseUrl + "/register", user, UserDTO.class);
        //UserDTO response = restTemplate.postForObject(baseUrl + "/register", user, UserDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("username123", responseBody.getUsername());
    }


//    @Test
//    public void testLoginSuccess() {
//        User user = new User("username100", "Password111");
//        restTemplate.postForEntity(baseUrl + "/register", user, User.class);
//
//        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl + "/login", user, User.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("username100", response.getBody().getUsername());
//
//    }

//    @Test
//    public void testLoginFailure() {
//        User invalidUser = new User("username2", "wrongPassword11");
//        try {
//            restTemplate.postForEntity(baseUrl + "/login", invalidUser, User.class);
//            fail("Should have thrown an HttpServerErrorException");
//        } catch (HttpServerErrorException e) {
//            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
//        }
//    }

//    @Test
//    public void testCalculateTotalBalance() {
//        Long userId = 1L;
//        ResponseEntity<BigInteger> response = restTemplate.getForEntity(
//                baseUrl + "/" + userId + "/total-balance",
//                BigInteger.class
//        );
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        List<Account> accounts = accountRepository.findByOwnerId(userId);
//        BigInteger expectedTotalBalance = accounts.stream()
//                .map(Account::getBalance)
//                .reduce(BigInteger.ZERO, BigInteger::add);
//
//        assertEquals(expectedTotalBalance, response.getBody());
//    }

}


