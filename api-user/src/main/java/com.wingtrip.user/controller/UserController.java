package com.wingtrip.user.controller;

import com.wingtrip.user.controller.mapper.UserMapper;
import com.wingtrip.user.controller.request.UserRequest;
import com.wingtrip.user.controller.response.UserResponse;
import com.wingtrip.user.controller.response.UserResponseWithoutMessage;
import com.wingtrip.user.dto.UserDTO;
import com.wingtrip.user.exception.*;
import com.wingtrip.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "User API")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final UserMapper userMapper;

    private final UserServiceImpl userService;

    @Operation(summary = "Get all the users")
    @GetMapping("/get-all")
    public ResponseEntity<List<UserResponseWithoutMessage>> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        List<UserResponseWithoutMessage> userResponses = userDTOS.stream()
                .map(userMapper::toResponseWithoutMessage)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    @Operation(summary = "Created new user")
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) throws UserNotCreateException {
        try {
            //Mappear de request a dto
            UserDTO dtoToRequest = userMapper.toDTO(userRequest);

            log.info("Checking if email exists {}:", dtoToRequest.getEmail());
            if (userService.existByEmail(dtoToRequest.getEmail())) {
                throw new UserNotCreateException(MessageCode.EMAIL_CREATE_BEFORE);
            }

            if (userService.existByUsername(dtoToRequest.getUsername())) {
                throw new UserNotCreateException(MessageCode.USERNAME_CREATE_BEFORE);
            }
            UserDTO createdUser = userService.createUser(dtoToRequest);
            UserResponse userResponse = userMapper.toResponse(createdUser);
            userResponse.setMessage("¡Created user successfully!");

            log.info("Created user successfully with data: {}", userResponse);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            throw new UserNotCreateException(MessageCode.USER_NOT_CREATE);
        }

    }

    @Operation(summary = "Search user by username")
    @GetMapping("/profile/{username}")
    public ResponseEntity<UserResponse> findByUsername(@PathVariable String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.findByUsername(username);
        UserResponse userResponse = userMapper.toResponse(userDTO);
        userResponse.setMessage("Find user successfully with username: " + username);

        log.info("Find user by username successfully with data: {}" + userResponse);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Search user by ID")
    @GetMapping("/find/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id) throws UserIdNotFoundException {
        UserDTO userDTO = userService.findUserById(id);
        UserResponse userResponse = userMapper.toResponse(userDTO);
        userResponse.setMessage("Find user by ID successfully with ID: " + id);

        log.info("Find user by ID successfully with data: {}", userResponse);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Update the user by username")
    @PutMapping("/update/username/{username}")
    public ResponseEntity<UserResponse> updateUserByUsername(@PathVariable String username, @RequestBody UserRequest userRequest) throws UsernameNotFoundException {
        UserDTO updateUser = userService.updateUserByUsername(username, userRequest);
        UserResponse userResponse = userMapper.toResponse(updateUser);
        userResponse.setMessage("Update user successfully with username: " + username);

        log.info("Update account successfully with data: {}", username);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Update the user by ID")
    @PutMapping("/update/id/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable Long id, @RequestBody UserRequest userRequest) throws UserIdNotFoundException {
        UserDTO toRequest = userService.updateUserById(id, userRequest);
        UserResponse userResponse = userMapper.toResponse(toRequest);
        userResponse.setMessage("Update user by ID successfully with ID: " + id);

        log.info("Update user by ID account successfully with data: {}" + userResponse);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Exist by email")
    @GetMapping("/existByEmail")
    public ResponseEntity<Boolean> existByEmail(@PathVariable String email) throws EmailNotFoundException, EmailAlreadyExistsException {
        boolean existed = userService.existByEmail(email);

        if (existed) {
            log.info("The email already exists: " + email);
        }
        return ResponseEntity.ok(existed);
    }

    @Operation(summary = "Exist by username")
    @GetMapping("/existsByUsername")
    public ResponseEntity<Boolean> existByUsername(@PathVariable String username) throws UsernameNotFoundException, UsernameAlreadyExistsException {
        boolean existed = userService.existByUsername(username);

        if (existed) {
            log.info("The username already exists: " + username);
        }
        return ResponseEntity.ok(existed);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) throws UserDeleteFailedException {
        boolean isDeleted = userService.deleteUserById(id);

        if (isDeleted) {
            log.info("Delete by user ID successfully with data: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            throw new UserDeleteFailedException(MessageCode.USER_DELETE_FAILED);
        }

    }
}
