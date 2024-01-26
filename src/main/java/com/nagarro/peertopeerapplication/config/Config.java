package com.nagarro.peertopeerapplication.config;

import com.nagarro.peertopeerapplication.services.AccountService;
import com.nagarro.peertopeerapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {


    @Bean
    public AccountService accountService(){
        return new AccountService();
    }

    @Bean
    public UserService userService(AccountService accountService){
        return new UserService(accountService);
    }

}
