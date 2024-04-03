package com.nagarro.peertopeerapplication.controller;

import com.nagarro.peertopeerapplication.assembler.UserModelAssembler;
import com.nagarro.peertopeerapplication.exception.UserNotFoundException;
import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    private final UserRepository userRepository;

    public UserController(UserService userService, UserModelAssembler userModelAssembler, UserRepository repository) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.userRepository = repository;
    }



    @Operation(summary = "Register User", description = "Registers a new user with the provided username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username or password"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/register")
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
    public ResponseEntity<UserDTO> logIn(@RequestBody UserDTO user) {
        UserDTO loggedInUser = userService.logIn(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }

    @GetMapping("/{userId}/total-balance")
    public ResponseEntity<BigInteger> getTotalBalance(@PathVariable Long userId) {
        BigInteger totalBalance = userService.calculateTotalBalance(userId);
        return new ResponseEntity<>(totalBalance, HttpStatus.OK);
    }

    @Operation(summary = "Get Specific User", description = "Retrieve details of a specific user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}")
    public EntityModel<User> getSpecificUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userModelAssembler.toModel(user);
    }

    @Operation(summary = "Get All Users", description = "Retrieve details of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/")
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
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
