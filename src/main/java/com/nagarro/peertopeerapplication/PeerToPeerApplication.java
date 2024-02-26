package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.config.Config;
import com.nagarro.peertopeerapplication.services.AccountService;
import com.nagarro.peertopeerapplication.services.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class PeerToPeerApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        UserService userService = context.getBean(UserService.class);
        AccountService accountService = context.getBean(AccountService.class);
        ResourceLoader resourceLoader = context;
        Resource resource = resourceLoader.getResource("classpath:userData.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInformations = line.split(" ");
                //userService.registerUser(userInformations[0], userInformations[1], userInformations[2]);
                //userService = new UserService(accountService);
               // userService.getAccountService().createAccount(userInformations[0], userInformations[4]);
                //accountService.deposit("0", Float.parseFloat(userInformations[3]));
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        context.close();
       // userService.registerUser("userid1", "userName", "Password1");

    }
}
