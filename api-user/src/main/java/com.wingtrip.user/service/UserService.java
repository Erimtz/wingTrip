package com.wingtrip.user.service;

import com.wingtrip.user.controller.request.UserRequest;
import com.wingtrip.user.dto.UserDTO;
import com.wingtrip.user.exception.*;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO createUser(UserDTO userDTO) throws UserNotCreateException;
    UserDTO findByUsername(String username) throws UsernameNotFoundException;
    UserDTO findUserById(Long userId) throws UserIdNotFoundException;
    UserDTO updateUserById(Long userId, UserRequest userRequest) throws UserIdNotFoundException;
    UserDTO updateUserByUsername(String username, UserRequest userRequest) throws UsernameNotFoundException;
    boolean existByEmail(String email) throws EmailNotFoundException, EmailAlreadyExistsException;
    boolean existByUsername(String username) throws UsernameNotFoundException, UsernameAlreadyExistsException;
    boolean deleteUserById(Long userId) throws UserDeleteFailedException;
}
