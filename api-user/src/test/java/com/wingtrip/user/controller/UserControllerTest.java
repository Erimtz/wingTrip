package com.wingtrip.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingtrip.user.controller.mapper.UserMapper;
import com.wingtrip.user.controller.request.UserRequest;
import com.wingtrip.user.controller.response.UserResponse;
import com.wingtrip.user.controller.response.UserResponseWithoutMessage;
import com.wingtrip.user.dto.UserDTO;
import com.wingtrip.user.service.impl.UserServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {UserController.class})
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.cloud.config.enable=false",
        "spring.cloud.config.discovery.enabled=false",
        "spring.cloud.config.fail-fast=false"
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserServiceImpl userService;

    private EasyRandom easyRandom;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        easyRandom = new EasyRandom();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllUsers_ShowReturnListOfUsers() throws Exception {
        //Given
        List<UserDTO> userDTOs = easyRandom.objects(UserDTO.class, 3).toList();
        List<UserResponseWithoutMessage> userResponses = userDTOs.stream()
                        .map(dto -> {
                            UserResponseWithoutMessage response = new UserResponseWithoutMessage();
                            response.setId(dto.getId());
                            response.setName(dto.getName());
                            response.setLastname(dto.getLastname());
                            response.setEmail(dto.getEmail());
                            response.setUsername(dto.getUsername());
                            return response;
                        })
                        .collect(Collectors.toList());

        when(userService.getAllUsers()).thenReturn(userDTOs);
        userDTOs.forEach(dto -> {
            UserResponseWithoutMessage response = userResponses.stream()
                    .filter(r -> r.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(null);
            when(userMapper.toResponseWithoutMessage(dto)).thenReturn(response);
        });

        //When & Then
        mockMvc.perform(get("/api/v1/user/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].email").exists());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreatedUser() throws Exception {
        //Given
        UserRequest userRequest = UserRequest.builder()
                .userId(1L)
                .name("Test")
                .lastname("User")
                .address("Test Address")
                .email("test@example.com")
                .username("testuser")
                .password("password123")
                .build();

        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserResponse userResponse = easyRandom.nextObject(UserResponse.class);

        when(userMapper.toDTO(userRequest)).thenReturn(userDTO);
        when(userService.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userService.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(userService.createUser(userDTO)).thenReturn(userDTO);
        when(userMapper.toResponse(userDTO)).thenReturn(userResponse);

        //When & Then
        mockMvc.perform(post("/api/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.username").exists());

        verify(userService, times(1)).existsByEmail(userDTO.getEmail());
        verify(userService, times(1)).existsByUsername(userDTO.getUsername());
        verify(userService, times(1)).createUser(userDTO);
    }

    @Test
    void createUser_WithExistingEmail_ShouldThrowException() throws Exception {
        //Given
        UserRequest userRequest = easyRandom.nextObject(UserRequest.class);
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);

        when(userMapper.toDTO(userRequest)).thenReturn(userDTO);
        when(userService.existsByEmail(userDTO.getEmail())).thenReturn(true);

        //When & Then
        mockMvc.perform(post("/api/v1/user/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).existsByEmail(userDTO.getEmail());
        verify(userService, never()).createUser(any());
    }

    @Test
    void findByUsername_WithValidUsername_ShouldReturnUser() throws Exception {
        //Given
        String username = "testuser";
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserResponse userResponse = easyRandom.nextObject(UserResponse.class);

        when(userService.findByUsername(username)).thenReturn(userDTO);
        when(userMapper.toResponse(userDTO)).thenReturn(userResponse);

        //When & Then
        mockMvc.perform(get("/api/v1/user/profile/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userResponse.getUsername()))
                .andExpect(jsonPath("$.message").exists());

        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void findUserById_WithValidId_ShouldReturnUser() throws Exception {
        //Given
        Long id = 1L;
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserResponse userResponse = easyRandom.nextObject(UserResponse.class);

        when(userService.findUserById(id)).thenReturn(userDTO);
        when(userMapper.toResponse(userDTO)).thenReturn(userResponse);

        //When & Then
        mockMvc.perform(get("/api/v1/user/find/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.message").exists());

        verify(userService, times(1)).findUserById(id);
    }

    @Test
    void updateUserByUsername_WithValidData_ShouldReturnUpdateUser() throws Exception {
        //Given
        String username = "testuser";
        UserRequest userRequest = easyRandom.nextObject(UserRequest.class);
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserResponse userResponse = easyRandom.nextObject(UserResponse.class);

        when(userService.updateUserByUsername(username, userRequest)).thenReturn(userDTO);
        when(userMapper.toResponse(userDTO)).thenReturn(userResponse);

        //When & Then
        mockMvc.perform(put("/api/v1/user/update/username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userResponse.getUsername()))
                .andExpect(jsonPath("$.message").exists());

        verify(userService, times(1)).updateUserByUsername(username, userRequest);
    }

    @Test
    void updateUserById_WithValidData_ShouldReturnUpdateUser() throws Exception {
        //Given
        Long id = 1L;
        UserRequest userRequest = easyRandom.nextObject(UserRequest.class);
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserResponse userResponse = easyRandom.nextObject(UserResponse.class);

        when(userService.updateUserById(id, userRequest)).thenReturn(userDTO);
        when(userMapper.toResponse(userDTO)).thenReturn(userResponse);

        //When & Then
        mockMvc.perform(put("/api/v1/user/update/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.message").exists());

        verify(userService, times(1)).updateUserById(id, userRequest);
    }

    @Test
    void validateEmail_WithAvailableEmail_ShouldReturnSuccess() throws Exception {
        //Given
        String email = "test@example.com";
        doNothing().when(userService).validateEmailNotExists(email);

        //When & Then
        mockMvc.perform(get("/api/v1/user/validateEmail/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.message").value("Email is available"))
                .andExpect(jsonPath("$.value").value(email));

        verify(userService, times(1)).validateEmailNotExists(email);
    }

    @Test
    void validateUsername_WithAvailableUsername_ShouldReturnSuccess() throws Exception {
        //Given
        String username = "testuser";
        doNothing().when(userService).validateUsernameNotExists(username);

        //When & Then
        mockMvc.perform(get("/api/v1/user/validateUsername/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.message").value("Username is available"))
                .andExpect(jsonPath("$.value").value(username));

        verify(userService, times(1)).validateUsernameNotExists(username);
    }

    @Test
    void existsByEmail_WithExistingEmail_ShouldReturnTrue() throws Exception {
        //Given
        String email = "existing@example.com";
        when(userService.existsByEmail(email)).thenReturn(true);

        //When & Then
        mockMvc.perform(get("/api/v1/user/existsByEmail/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.message").value("Email already exists"))
                .andExpect(jsonPath("$.value").value(email));

        verify(userService, times(1)).existsByEmail(email);
    }

    @Test
    void existsByUsername_WithExistingUsername_ShouldReturnTrue() throws Exception {
        //Given
        String username = "existingUser";
        when(userService.existsByUsername(username)).thenReturn(true);

        //When & Then
        mockMvc.perform(get("/api/v1/user/existsByUsername/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.message").value("Username already exists"))
                .andExpect(jsonPath("$.value").value(username));

        verify(userService, times(1)).existsByUsername(username);
    }

    @Test
    void deleteUserById_WithValidId_ShouldReturnNoContent() throws Exception {
        //Given
        Long id = 1L;
        when(userService.deleteUserById(id)).thenReturn(true);

        //When & Then
        mockMvc.perform(delete("/api/v1/user/delete/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario eliminado correctamente"))
                .andExpect(jsonPath("$.userId").value(id));

        verify(userService, times(1)).deleteUserById(id);
    }

    @Test
    void deleteUserById_WithInvalidId_ShouldReturnException() throws Exception {
        //Given
        Long id = 999L;
        when(userService.deleteUserById(id)).thenReturn(false);

        //When & Then
        mockMvc.perform(delete("/api/v1/user/delete/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).deleteUserById(id);
    }
}