package com.nagarro.peertopeerapplication;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.enums.Role;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.services.UserService;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
public class Integration {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserService userService;

//    @Test
//    @WithMockUser(username = "testUser", authorities = {"user:create"})
//    public void testLogin() throws Exception {
//
//        UserDTO user = new UserDTO("username123", "Password1130");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(user)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username123")); // Assuming the response contains the username
//    }
//
//    private String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    //-----------------------------
    @Autowired
    private WebApplicationContext applicationContext;
    private MockMvc mockMvc;

  ;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .defaultRequest(get("/api/v1/users").with(user("user").roles("USER")))
                .apply(springSecurity())
                .build();
    }



    @Test
    public void getLoggedInUser() throws Exception {
        mockMvc.perform(formLogin("/login").user("userTest").password("PasswordTestUser1"));
    }

}