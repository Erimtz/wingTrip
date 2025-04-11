package com.wingtrip.user.service.impl;

import com.wingtrip.user.controller.request.UserRequest;
import com.wingtrip.user.dto.UserDTO;
import com.wingtrip.user.exception.*;
import com.wingtrip.user.model.UserEntity;
import com.wingtrip.user.repository.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        easyRandom = new EasyRandom();
    }

    @Test
    void getAllUsers() {
        //Lista UserEntities con valores aleatorios
        List<UserEntity> userEntities = easyRandom.objects(UserEntity.class, 2).toList();

        //Crea una lista de UserDTOs esperados, asignando los valores correspondientes de UserEntity
        List<UserDTO> expectedUsers = userEntities.stream()
                        .map(userEntity -> new UserDTO(userEntity.getUserId(), userEntity.getName(),
                                userEntity.getLastname(), userEntity.getAddress(), userEntity.getEmail(),
                                userEntity.getUsername(), userEntity.getPassword()))
                        .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(userEntities);

        List<UserDTO> allUsers = userService.getAllUsers();


        Assertions.assertEquals(expectedUsers, allUsers);

        System.out.println("Usuarios esperados: ");
        expectedUsers.forEach(System.out::println);

        System.out.println("Usuarios reales: ");
        allUsers.forEach(System.out::println);
        
        if (expectedUsers.size() != allUsers.size()) {
            fail("Las listas tienen tamaños diferentes");
        }

        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEquals(expectedUsers.get(i), allUsers.get(i));
        }
    }

    @Test
    void createUser() throws UserNotCreateException {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);

        UserEntity userEntity = UserEntity.builder()
                .name(userDTO.getName())
                .lastname(userDTO.getLastname())
                .address(userDTO.getAddress())
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();

        when(userRepository.save(userEntity)).thenReturn(userEntity);
        UserDTO createdUser = userService.createUser(userDTO);

        Assertions.assertEquals(userDTO.getName(), createdUser.getName());
        Assertions.assertEquals(userDTO.getLastname(), createdUser.getLastname());
        Assertions.assertEquals(userDTO.getAddress(), createdUser.getAddress());
        Assertions.assertEquals(userDTO.getEmail(), createdUser.getEmail());
        Assertions.assertEquals(userDTO.getUsername(), createdUser.getUsername());
        Assertions.assertEquals(userDTO.getPassword(), createdUser.getPassword());

        verify(userRepository).save(userEntity);
    }

    @Test
    void findByUsername() throws UsernameNotFoundException {
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.ofNullable(userEntity));
        UserDTO getByUsername = userService.findByUsername(userDTO.getUsername());

        //Convierte UserEntity a UserDTO
        UserDTO expectedUserDTO = new UserDTO(userEntity);

        Assertions.assertEquals(getByUsername, expectedUserDTO);
    }

    @Test
    void findUserById() throws UserIdNotFoundException {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);

        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.ofNullable(userEntity));
        UserDTO getById = userService.findUserById(userDTO.getId());

        UserDTO expectedUserDTO = new UserDTO(userEntity);

        Assertions.assertEquals(getById, expectedUserDTO);
    }

    @Test
    void updateUserById() throws UserIdNotFoundException {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);
        userEntity.setUserId(userDTO.getId()); //Asigna el mismo ID que el del UserDTO
        UserRequest userRequest = easyRandom.nextObject(UserRequest.class);

        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        UserDTO updateById = userService.updateUserById(userDTO.getId(), userRequest);

        UserDTO expectedUserDTO = new UserDTO(userEntity);

        Assertions.assertEquals(updateById.getName(), expectedUserDTO.getName());
        Assertions.assertEquals(updateById.getLastname(), expectedUserDTO.getLastname());
        Assertions.assertEquals(updateById.getAddress(), expectedUserDTO.getAddress());
        Assertions.assertEquals(updateById.getEmail(), expectedUserDTO.getEmail());
        Assertions.assertEquals(updateById.getUsername(), expectedUserDTO.getUsername());
        Assertions.assertEquals(updateById.getPassword(), expectedUserDTO.getPassword());

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void updateUserByUsername() throws UsernameNotFoundException {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);
        userEntity.setUserId(userDTO.getId());
        UserRequest userRequest = easyRandom.nextObject(UserRequest.class);

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        UserDTO updateByUsername = userService.updateUserByUsername(userDTO.getUsername(), userRequest);

        UserDTO expectedUserDTO = new UserDTO(userEntity);

        Assertions.assertEquals(updateByUsername.getName(), expectedUserDTO.getName());
        Assertions.assertEquals(updateByUsername.getLastname(), expectedUserDTO.getLastname());
        Assertions.assertEquals(updateByUsername.getAddress(), expectedUserDTO.getAddress());
        Assertions.assertEquals(updateByUsername.getEmail(), expectedUserDTO.getEmail());
        Assertions.assertEquals(updateByUsername.getUsername(), expectedUserDTO.getUsername());
        Assertions.assertEquals(updateByUsername.getPassword(), expectedUserDTO.getPassword());

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void existByEmail() throws EmailNotFoundException, EmailAlreadyExistsException {
        String email = "pepito@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);
        userService.existByEmail(email);

        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void existByUsername() throws UsernameNotFoundException, UsernameAlreadyExistsException {
        String username = "pepito";

        when(userRepository.existsByUsername(username)).thenReturn(true);
        userService.existByUsername(username);

        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void deleteUserById() throws UserDeleteFailedException {
        Long userId = 12L;
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(userEntity));
        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}