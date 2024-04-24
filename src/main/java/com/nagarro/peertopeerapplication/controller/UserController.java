package com.nagarro.peertopeerapplication.controller;

import com.nagarro.peertopeerapplication.assembler.UserModelAssembler;
import com.nagarro.peertopeerapplication.exception.UserNotFoundException;
import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;
//    private final UserModelAssembler userModelAssembler;
//
//    private final UserRepository userRepository;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @Operation(summary = "Register User", description = "Registers a new user with the provided username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username or password"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('user:create')")
    public UserDTO registerUser(@RequestBody UserDTO user) {
        String username = user.getUsername();
        String password = user.getPassword();

        return userService.registerUser(username, password);
    }

    @Operation(summary = "Log in user", description = "Authenticate user with username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username or password"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/login")
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<UserDTO> logIn(@RequestBody UserDTO user) {
        UserDTO loggedInUser = userService.logIn(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }

    @Operation(summary = "Change password", description = "Change password for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username or password"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{userId}/password")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody String newPassword ){
        boolean passwordChanged = userService.changePassword(userId, newPassword);
        if(passwordChanged){
            return ResponseEntity.ok("Password changed successfully");
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change the password");
        }
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + ex.getMessage());
    }

    @ExceptionHandler({Exception.class,
            DataAccessException.class,
            ValidationException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + ex.getMessage());
    }

}
