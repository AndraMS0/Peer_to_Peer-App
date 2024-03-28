package com.nagarro.peertopeerapplication;

import com.nagarro.peertopeerapplication.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static com.nagarro.peertopeerapplication.enums.Role.ADMIN;
import static com.nagarro.peertopeerapplication.enums.Role.USER;
import com.nagarro.peertopeerapplication.auth.AuthenticationService;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PeerToPeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeerToPeerApplication.class, args);

    }

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService service) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .username("Admin")
                    .password("passwordAdmin1")
                    .role(ADMIN)
                    .build();
            System.out.println("Admin token: " + service.register(admin).getAccessToken());

            var user = RegisterRequest.builder()
                    .username("User")
                    .password("PasswordUser1")
                    .role(USER)
                    .build();
            System.out.println("Manager token: " + service.register(user).getAccessToken());

        };
    }
}
