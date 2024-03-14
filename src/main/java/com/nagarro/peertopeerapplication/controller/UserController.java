package com.nagarro.peertopeerapplication.controller;

import com.nagarro.peertopeerapplication.assembler.UserModelAssembler;
import com.nagarro.peertopeerapplication.custom.UserNotFoundException;
import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.model.User;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import com.nagarro.peertopeerapplication.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    private final UserRepository repository;

    public UserController(UserService userService, UserModelAssembler userModelAssembler, UserRepository repository) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.repository = repository;
    }

    //If I put the parameter for @RequestBody of type UserDTO my integration test fails
    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody User user) {
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
    public ResponseEntity<UserDTO> logIn(@RequestBody User user) {
        UserDTO loggedInUser = userService.logIn(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }

    @GetMapping("/{userId}/total-balance")
    public ResponseEntity<BigInteger> getTotalBalance(@PathVariable Long userId) {
        BigInteger totalBalance = userService.calculateTotalBalance(userId);
        return new ResponseEntity<>(totalBalance, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public EntityModel<User> one(@PathVariable Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userModelAssembler.toModel(user);
    }

    @GetMapping("/")
    public CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> users = repository.findAll().stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
