package com.wingtrip.user.controller.doc;

import com.wingtrip.user.controller.request.UserRequest;
import com.wingtrip.user.controller.response.DeleteResponse;
import com.wingtrip.user.controller.response.ExistenceResponse;
import com.wingtrip.user.controller.response.UserResponse;
import com.wingtrip.user.controller.response.UserResponseWithoutMessage;
import com.wingtrip.user.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Tag(name = "Users", description = "User management API - WingTrip User Service")
public interface UserControllerDoc {

    @Operation(summary = "Get all the users",
            description = "Returns a complete list of all registered users in the system")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users list retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseWithoutMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    ResponseEntity<List<UserResponseWithoutMessage>> getAllUsers();


    @Operation(summary = "Created new user",
            description = "Creates a new user in the system. Email and username must be unique.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or email/username already exists",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error creating user",
                    content = @Content
            )
    })
    ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) throws UserNotCreateException;


    @Operation(summary = "Search user by username",
            description = "Search and return the information for a user using their username.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the provided username",
                    content = @Content
            )
    })
    ResponseEntity<UserResponse> findByUsername(@PathVariable String username) throws UsernameNotFoundException;


    @Operation(summary = "Search user by ID",
            description = "Search and return the information for a specific user using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the provided ID",
                    content = @Content
            )
    })
    ResponseEntity<UserResponse> findUserById(@PathVariable Long id) throws UserIdNotFoundException;


    @Operation(summary = "Update user by username",
            description = "Updates an existing user's information using their username.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid update data",
                    content = @Content
            )
    })
    ResponseEntity<UserResponse> updateUserByUsername(@PathVariable String username, @RequestBody UserRequest userRequest) throws UsernameNotFoundException;


    @Operation(summary = "Update user by ID",
            description = "Updates and existing user's information using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User update successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid update data",
                    content = @Content
            )
    })
    ResponseEntity<UserResponse> updateUserById(@PathVariable Long id, @RequestBody UserRequest userRequest) throws UserIdNotFoundException;


    @ApiResponse(
            responseCode = "200",
            description = "Verification completed successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExistenceResponse.class)
            )
    )
    ResponseEntity<ExistenceResponse> validateEmail(@PathVariable String email) throws EmailNotFoundException, EmailAlreadyExistsException;


    @ApiResponse(
            responseCode = "200",
            description = "Verification completed successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExistenceResponse.class)
            )
    )
    ResponseEntity<ExistenceResponse> validateUsername(@PathVariable String username) throws UsernameNotFoundException, UsernameAlreadyExistsException;



    @Operation(summary = "Exist by email",
            description = "Check if a registered user already exists with the provided email address.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Verification completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid email",
                    content = @Content
            )
    })
    ResponseEntity<ExistenceResponse> existByEmail(@PathVariable String email);


    @Operation(summary = "Exist by username",
            description = "Checks if a registered user already exists with the provided username.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Verification completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid username",
                    content = @Content
            )
    })
    ResponseEntity<ExistenceResponse> existByUsername(@PathVariable String username);


    @Operation(summary = "Delete user by ID",
            description = "Permanently deletes a user from the system using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully deleted",
                    content = @Content(
                    mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error deleting user",
                    content = @Content
            )
    })
    ResponseEntity<DeleteResponse> deleteUserById(@PathVariable Long id) throws UserDeleteFailedException;
}
