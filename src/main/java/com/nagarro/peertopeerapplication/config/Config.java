package com.nagarro.peertopeerapplication.config;

import com.nagarro.peertopeerapplication.repositories.AccountRepository;
import com.nagarro.peertopeerapplication.repositories.GenericUserRepository;
import com.nagarro.peertopeerapplication.repositories.TransactionRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
//<<<<<<< Updated upstream
import com.nagarro.peertopeerapplication.services.AccountService;
import com.nagarro.peertopeerapplication.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class Config {
    private int initialAccountId;
}
//=======
//import com.nagarro.peertopeerapplication.services.UserService;
//import com.nagarro.peertopeerapplication.services.AccountService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class Config {
//    @Value("${account.initialId:0}")
//    private Long initialAccountId;
//>>>>>>> Stashed changes
//
//    //    @Value("#{systemProperties['spring.profiles.active'] == 'prod' ? '${account.initialId.prod}' : '${account.initialId.dev}'}")
//    //    private int initialAccountId;
//    @Value("${account.initialId.dev:0}")
//<<<<<<< Updated upstream
//    private void setDevInitialAccountId(int id) {
//=======
//    private void setDevInitialAccountId(Long id) {
//>>>>>>> Stashed changes
//        this.initialAccountId = id;
//    }
//
//    @Value("${account.initialId.prod:0}")
//<<<<<<< Updated upstream
//    private void setProdInitialAccountId(int id) {
//        this.initialAccountId = id;
//    }
//
//    @Bean
//    public AccountService accountService(AccountRepository accountRepository, UserRepository userRepository) {
//        AccountService service = new AccountService(accountRepository, userRepository);
//        //service.setInitialAccountId(initialAccountId);
//        return service;
//    }
//
//    @Bean
//    public UserService userService(UserRepository userRepository, AccountService accountService, TransactionRepository transactionRepository, GenericUserRepository genericUserRepository) {
//        return new UserService(userRepository, accountService, transactionRepository, genericUserRepository);
//    }
//=======
//    private void setProdInitialAccountId(Long id) {
//        this.initialAccountId = id;
//    }
//
////    @Bean
////    public AccountService accountService(AccountRepository accountRepository, UserRepository userRepository) {
////        AccountService service = new AccountService(accountRepository, userRepository);
////        //service.setInitialAccountId(initialAccountId);
////        return service;
////    }
//
////    @Bean
////    public UserService userService(UserRepository userRepository, AccountService accountService, TransactionRepository transactionRepository, GenericUserRepository genericUserRepository) {
////        return new UserService(userRepository, accountService, transactionRepository, genericUserRepository);
////    }
//>>>>>>> Stashed changes
//
//}
