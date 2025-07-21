package com.wingtrip.user.service.impl;

import com.wingtrip.user.controller.request.UserRequest;
import com.wingtrip.user.dto.UserDTO;
import com.wingtrip.user.exception.*;
import com.wingtrip.user.model.UserEntity;
import com.wingtrip.user.repository.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        easyRandom = new EasyRandom();
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDtos() {
        //Given
        List<UserEntity> userEntities = easyRandom.objects(UserEntity.class, 3).toList();
        when(userRepository.findAll()).thenReturn(userEntities);

        //When
        List<UserDTO> result = userService.getAllUsers();

        //Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(userEntities.get(0).getUserId());
        assertThat(result.get(0).getName()).isEqualTo(userEntities.get(0).getName());
        assertThat(result.get(0).getEmail()).isEqualTo(userEntities.get(0).getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_WhenNoUsers_ShouldReturnEmptyList() {
        //Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        //When
        List<UserDTO> result = userService.getAllUsers();

        //Then
        assertThat(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreatedUser() throws UserNotCreateException {
        //Given
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserEntity userEntity = UserEntity.builder()
                .name(userDTO.getName())
                .lastname(userDTO.getLastname())
                .address(userDTO.getAddress())
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();

        UserEntity savedEntity = UserEntity.builder()
                .userId(1L)
                .name(userDTO.getName())
                .lastname(userDTO.getLastname())
                .address(userDTO.getAddress())
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        //When
        UserDTO result = userService.createUser(userDTO);

        //Then
        assertThat(result.getId()).isEqualTo(savedEntity.getUserId());
        assertThat(result.getName()).isEqualTo(userDTO.getName());
        assertThat(result.getLastname()).isEqualTo(userDTO.getLastname());
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getUsername()).isEqualTo(userDTO.getUsername());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void createUser_WhenRepositoryFails_ShouldThrowException() {
        //Given
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        when(userRepository.save(any(UserEntity.class))).thenThrow(new RuntimeException("Database Error"));

        //When && Then
        assertThatThrownBy(()-> userService.createUser(userDTO))
                .isInstanceOf(UserNotCreateException.class);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void findByUsername_WithExistingUsername_ShouldReturnUserDTO() throws UsernameNotFoundException {
        //Given
        String username = "testuser";
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);
        userEntity.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        //When
        UserDTO result = userService.findByUsername(username);

        //Then
        assertThat(result.getId()).isEqualTo(userEntity.getUserId());
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getName()).isEqualTo(userEntity.getName());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_WithNonExistingUsername_ShouldThrowException() {
        //Given
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        //When & Then
        assertThatThrownBy(() -> userService.findByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findUserById_WithExistingId_ShouldReturnUserDTO() throws UserIdNotFoundException {
        //Given
        Long id = 1L;
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);
        userEntity.setUserId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));

        //When
        UserDTO result = userService.findUserById(id);

        //Then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(userEntity.getName());
        assertThat(result.getEmail()).isEqualTo(userEntity.getEmail());

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void findUserById_WithNonExistingId_ShouldThrowException() {
        //Given
        Long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //When && Then
        assertThatThrownBy(() -> userService.findUserById(id))
                .isInstanceOf(UserIdNotFoundException.class);

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void updateUserById_WithValidData_ShouldReturnUpdateUser() throws UserIdNotFoundException {
        //Given
        Long id = 1L;
        UserEntity existingEntity = easyRandom.nextObject(UserEntity.class);
        existingEntity.setUserId(id);

        UserRequest userRequest = UserRequest.builder()
                .userId(id)
                .name("Updated name")
                .lastname("Updated lastname")
                .address("Updated address")
                .email("updated@example.com")
                .username("updateuser")
                .password("newpassword")
                .build();

        UserEntity updateEntity = UserEntity.builder()
                .userId(id)
                .name(userRequest.getName())
                .lastname(userRequest.getLastname())
                .address(userRequest.getAddress())
                .email(userRequest.getEmail())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updateEntity);

        //When
        UserDTO result = userService.updateUserById(id, userRequest);

        //Then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(userRequest.getName());
        assertThat(result.getEmail()).isEqualTo(userRequest.getEmail());

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateUserByUsername_WithValidData_ShouldReturnUpdateUser() throws UsernameNotFoundException {
        //Given
        String username = "testuser";
        UserEntity existingEntity = easyRandom.nextObject(UserEntity.class);
        existingEntity.setUsername(username);

        UserRequest userRequest = easyRandom.nextObject(UserRequest.class);

        UserEntity updateEntity = UserEntity.builder()
                .userId(existingEntity.getUserId())
                .name(userRequest.getName())
                .lastname(userRequest.getLastname())
                .address(userRequest.getAddress())
                .email(userRequest.getEmail())
                .username(existingEntity.getUsername())
                .password(userRequest.getPassword())
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updateEntity);

        //When
        UserDTO result = userService.updateUserByUsername(username, userRequest);

        //Then
        assertThat(result.getId()).isEqualTo(existingEntity.getUserId());
        assertThat(result.getName()).isEqualTo(userRequest.getName());
        assertThat(result.getEmail()).isEqualTo(userRequest.getEmail());

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void existsByEmail_WithExistingEmail_ShouldReturnTrue() {
        //Given
        String email = "existing@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        //When
        boolean result = userService.existsByEmail(email);

        //Then
        assertThat(result).isTrue();
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void existsByEmail_WithExistingEmail_ShouldReturnFalse() {
        //Given
        String email = "nonexitent@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        //When
        boolean result = userService.existsByEmail(email);

        //Then
        assertThat(result).isFalse();
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void existsByUsername_WithExistingUsername_ShouldReturnTrue() {
        //Given
        String username = "existinguser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        //When
        Boolean result = userService.existsByUsername(username);

        //Then
        assertThat(result).isTrue();
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void existsByUsername_WithExistingUsername_ShouldReturnFalse() {
        //Given
        String username = "nonexistentuser";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        //When
        Boolean result = userService.existsByUsername(username);

        //Then
        assertThat(result).isFalse();
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void validateEmailNotExists_WithAvailableEmail_ShouldNotThrowException() {
        //Given
        String email = "available@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        //When & Then
        assertThatCode(() -> userService.validateEmailNotExists(email))
                .doesNotThrowAnyException();

        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void validateEmailNotExists_WithExistingEmail_ShouldThrowException() {
        //Given
        String email = "existing@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        //When & Then
        assertThatThrownBy(() -> userService.validateEmailNotExists(email))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, times(1)).existsByEmail(email);

    }

    @Test
    void validateUsernameNotExists_WithAvailableUsername_ShouldNotThrowException() {
        //Given
        String username = "availableuser";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        //When & Then
        assertThatCode(() -> userService.validateUsernameNotExists(username))
                .doesNotThrowAnyException();

        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void validateUsernameNotExist_WithExistingUsername_ShouldThrowException() {
        //Given
        String username = "existingusername";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        //When & Then
        assertThatThrownBy(() -> userService.validateUsernameNotExists(username))
                .isInstanceOf(UsernameAlreadyExistsException.class);

        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void deleteUserById_WithExistingId_ShouldReturnTrue() throws UserDeleteFailedException {
        //Given
        Long id = 1L;
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);
        userEntity.setUserId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(id);

        //When
        Boolean result = userService.deleteUserById(id);

        //Then
        assertThat(result).isTrue();
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteUserById_WithNonExistingId_ShouldThrowException() {
        //Given
        Long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //When & Then
        assertThatThrownBy(() -> userService.deleteUserById(id))
                .isInstanceOf(UserDeleteFailedException.class);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, never()).deleteById(id);
    }

    @Test
    void deleteUserById_WhenRepositoryFails_ShouldThrowException() {
        //Given
        Long id = 1L;
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);
        userEntity.setUserId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        doThrow(new RuntimeException("Database Error")).when(userRepository).deleteById(id);

        //When & Then
        assertThatThrownBy(() -> userService.deleteUserById(id))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).deleteById(id);
    }
}