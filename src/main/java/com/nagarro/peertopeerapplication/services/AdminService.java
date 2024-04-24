package com.nagarro.peertopeerapplication.services;

import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.enums.Role;
import com.nagarro.peertopeerapplication.model.Token;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.TokenRepository;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public UserDTO deleteUser(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            List<Token> tokens = user.getTokens();
            for(Token token: tokens){
                tokenRepository.delete(token);
            }
            userRepository.delete(user);
            return new UserDTO(user.getUsername(), user.getPassword());
        }else{
            return null;
        }
    }

    public List<UserDTO> getAllUsers(){
       List<User> allUsers = userRepository.findAll();
       List<UserDTO> usersDTO = new ArrayList<>();
       for(User user: allUsers){
           usersDTO.add(new UserDTO(user.getUsername(), user.getPassword()));
       }
       return usersDTO;
    }

    public UserDTO createAdmin(String username, String password){
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            if(user.getRole().equals("ADMIN")){
                throw new IllegalArgumentException("Admin already exists with this username.");
            }
        }
        User admin = new User(username, passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        return new UserDTO(admin.getUsername(), admin.getPassword());
    }

}
