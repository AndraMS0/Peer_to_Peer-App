package com.nagarro.peertopeerapplication.controller;

import com.nagarro.peertopeerapplication.assembler.UserModelAssembler;
import com.nagarro.peertopeerapplication.dto.AccountDTO;
import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.exception.UserNotFoundException;
import com.nagarro.peertopeerapplication.services.AccountService;
import com.nagarro.peertopeerapplication.services.AdminService;
import io.swagger.v3.oas.annotations.Hidden;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AccountService accountService;
    private final AdminService adminService;
    private final UserModelAssembler userModelAssembler;

    public AdminController(AccountService accountService, AdminService adminService, UserModelAssembler userModelAssembler) {
        this.accountService = accountService;
        this.adminService = adminService;
        this.userModelAssembler = userModelAssembler;
    }

    @Operation(summary = "Create Admin", description = "Registers a new admin with the provided username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin registered successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username or password"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public UserDTO createAdmin(@RequestBody UserDTO user){
        UserDTO adminUser = adminService.createAdmin(user.getUsername(), user.getPassword());
        return adminUser;
    }

    @Operation(summary = "Get All Users", description = "Retrieve details of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('admin:read')")
    public CollectionModel<EntityModel<UserDTO>> getAllUsers() {
        List<EntityModel<UserDTO>> users = adminService.getAllUsers().stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(AdminController.class).getAllUsers()).withSelfRel());
    }

    @Operation(summary = "Delete a specific user", description = "Delete a certain user by received id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @Hidden
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long userId) {
        UserDTO deletedUser = adminService.deleteUser(userId);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);

    }

    @Operation(summary = "Get a list of financial accounts that a user has", description = "Delete a certain user by received id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getAccounts/{userId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<AccountDTO>> getAccountsByOwnerId(@PathVariable Long userId) {
        List<AccountDTO> totalBalance = accountService.getAccountsByOwnerId(userId);
        return new ResponseEntity<>(totalBalance, HttpStatus.OK);
    }

    @ExceptionHandler({Exception.class,
            DataAccessException.class,
            ValidationException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
