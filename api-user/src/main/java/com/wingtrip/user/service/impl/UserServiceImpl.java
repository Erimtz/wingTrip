package com.wingtrip.user.service.impl;

import com.wingtrip.user.controller.request.UserRequest;
import com.wingtrip.user.dto.UserDTO;
import com.wingtrip.user.exception.*;
import com.wingtrip.user.model.UserEntity;
import com.wingtrip.user.repository.UserRepository;
import com.wingtrip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userEntity -> {
                    UserDTO userDTO = new UserDTO(userEntity);
                    userDTO.setId(userEntity.getUserId());
                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws UserNotCreateException {
        try {
            UserEntity userEntity = UserEntity.builder()
                    .name(userDTO.getName())
                    .lastname(userDTO.getLastname())
                    .address(userDTO.getAddress())
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .build();
            UserEntity saveEntity = userRepository.save(userEntity);
            return new UserDTO(saveEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserNotCreateException(MessageCode.USER_NOT_CREATE);
        }
    }

    @Override
    public UserDTO findByUsername(String username) throws UsernameNotFoundException {
        if (username == null) {
            throw new UsernameNotFoundException(MessageCode.USERNAME_NULL);
        }

        Optional<UserEntity> entityOptional = userRepository.findByUsername(username);
        if (entityOptional.isPresent()) {
            return new UserDTO(entityOptional.get());
        } else {
            throw new UsernameNotFoundException(MessageCode.USER_NOT_EXIST);
        }
    }

    @Override
    public UserDTO findUserById(Long userId) throws UserIdNotFoundException {
        if (userId == null) {
            throw new UserIdNotFoundException(MessageCode.USER_ID_NOT_FOUND);
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserIdNotFoundException(MessageCode.USER_ID_NOT_FOUND);
        }

        UserEntity userEntity = optionalUser.get();
        return new UserDTO(userEntity);
    }

    @Override
    public UserDTO updateUserById(Long userId, UserRequest userRequest) throws UserIdNotFoundException {
        if (userId == null) {
            throw new UserIdNotFoundException(MessageCode.USERNAME_NULL);
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserIdNotFoundException(MessageCode.USER_ID_NOT_FOUND);
        }

        UserEntity userEntity = optionalUser.get();
        userEntity.setName(userRequest.getName());
        userEntity.setLastname(userRequest.getLastname());
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setPassword(userRequest.getPassword());
        userEntity.setAddress(userRequest.getAddress());

        UserEntity userEntity1 = userRepository.save(userEntity);

        return new UserDTO(userEntity1);
    }

    @Override
    public UserDTO updateUserByUsername(String username, UserRequest userRequest) throws UsernameNotFoundException {
        if (username == null) {
            throw new UsernameNotFoundException(MessageCode.USERNAME_NULL);
        }

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException(MessageCode.USER_NOT_FOUND);
        }

        UserEntity userEntity1 = userEntity.get();
        userEntity1.setName(userRequest.getName());
        userEntity1.setLastname(userRequest.getLastname());
        userEntity1.setEmail(userRequest.getEmail());
        userEntity1.setUsername(userRequest.getUsername());
        userEntity1.setPassword(userRequest.getPassword());

        UserEntity updateEntity = userRepository.save(userEntity1);

        return new UserDTO(updateEntity);
    }

    @Override
    public void validateEmailNotExists(String email) throws EmailNotFoundException, EmailAlreadyExistsException {
        if (email == null || email.isBlank()) {
            throw new EmailNotFoundException(MessageCode.EMAIL_USER_NOT_FOUND);
        }

        // Verificar si ya existe en la base de datos
        boolean exists = userRepository.existsByEmail(email);
        if (exists) {
            throw new EmailAlreadyExistsException(MessageCode.EMAIL_CREATE_BEFORE);
        }
    }

    @Override
    public void validateUsernameNotExists(String username) throws UsernameNotFoundException, UsernameAlreadyExistsException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException(MessageCode.USERNAME_NOT_FOUND);
        }

        // Verificar si ya existe en la base de datos
        boolean exists = userRepository.existsByUsername(username);
        if (exists) {
            throw new UsernameAlreadyExistsException(MessageCode.USERNAME_CREATE_BEFORE);
        }
    }

    @Override
    public boolean existByEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existByUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean deleteUserById(Long userId) throws UserDeleteFailedException {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UserDeleteFailedException(MessageCode.USER_ID_NOT_FOUND);
        } else {
            userRepository.deleteById(userId);
            return true;
        }
    }
}
