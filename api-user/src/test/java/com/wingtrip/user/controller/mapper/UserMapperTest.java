package com.wingtrip.user.controller.mapper;

import com.wingtrip.user.controller.request.UserRequest;
import com.wingtrip.user.controller.response.UserResponse;
import com.wingtrip.user.controller.response.UserResponseWithoutMessage;
import com.wingtrip.user.dto.UserDTO;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private EasyRandom easyRandom;
    
    @BeforeEach
    void setUp() {
        easyRandom = new EasyRandom();
    }
     
    
    @Test
    void toDTO_WithValidUserRequest_ShouldMapCorrectly() {
        //Given
         UserRequest userRequest = UserRequest.builder()
                 .userId(1L)
                 .name("John")
                 .lastname("Doe")
                 .address("123 Main")
                 .email("john@example.com")
                 .username("johndoe")
                 .password("password123")
                 .build();

        //When
        UserDTO result = userMapper.toDTO(userRequest);

        //Then
        assertThat(result.getId()).isEqualTo(userRequest.getUserId());
        assertThat(result.getName()).isEqualTo(userRequest.getName());
        assertThat(result.getLastname()).isEqualTo(userRequest.getLastname());
        assertThat(result.getAddress()).isEqualTo(userRequest.getAddress());
        assertThat(result.getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(result.getUsername()).isEqualTo(userRequest.getUsername());
        assertThat(result.getPassword()).isEqualTo(userRequest.getPassword());
    }

    @Test
    void toDTO_WithNullUserRequest_ShouldReturnNull() {
        //Given
        UserRequest userRequest = null;

        //When
        UserDTO result = userMapper.toDTO(userRequest);

        //Then
        assertThat(result).isNull();

    }

    @Test
    void toDTO_WithRandomUserRequest_ShouldMapAllFields() {
        //Given
        UserRequest userRequest = easyRandom.nextObject(UserRequest.class);

        //When
        UserDTO result = userMapper.toDTO(userRequest);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userRequest.getUserId());
        assertThat(result.getName()).isEqualTo(userRequest.getName());
        assertThat(result.getLastname()).isEqualTo(userRequest.getLastname());
        assertThat(result.getAddress()).isEqualTo(userRequest.getAddress());
        assertThat(result.getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(result.getUsername()).isEqualTo(userRequest.getUsername());
        assertThat(result.getPassword()).isEqualTo(userRequest.getPassword());
    }

    @Test
    void toResponse_WithValidUserDTO_ShouldMapCorrectly() {
        //Given
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .name("Jane")
                .lastname("Smith")
                .address("avenida 456")
                .email("jane@example.com")
                .username("janesmith")
                .password("secret123")
                .build();

        //When
        UserResponse result = userMapper.toResponse(userDTO);

        //Then
        assertThat(result.getId()).isEqualTo(userDTO.getId());
        assertThat(result.getName()).isEqualTo(userDTO.getName());
        assertThat(result.getLastname()).isEqualTo(userDTO.getLastname());
        assertThat(result.getAddress()).isEqualTo(userDTO.getAddress());
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getUsername()).isEqualTo(userDTO.getUsername());
        assertThat(result.getMessage()).isNull();
    }

    @Test
    void toResponse_WithNullUserDTO_ShouldReturnNull() {
        //Given
        UserDTO userDTO = null;

        //When
        UserResponse result = userMapper.toResponse(userDTO);

        //Then
        assertThat(result).isNull();
    }

    @Test
    void toResponse_WithRandomUserDTO_ShouldMapAllFields() {
        //Given
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);

        //When
        UserResponse result = userMapper.toResponse(userDTO);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDTO.getId());
        assertThat(result.getName()).isEqualTo(userDTO.getName());
        assertThat(result.getLastname()).isEqualTo(userDTO.getLastname());
        assertThat(result.getAddress()).isEqualTo(userDTO.getAddress());
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getUsername()).isEqualTo(userDTO.getUsername());
    }

    @Test
    void toResponseWithoutMessage_WithValidUserDTO_ShouldMapCorrectly() {
        //Given
        UserDTO userDTO = UserDTO.builder()
                .id(2L)
                .name("Bob")
                .lastname("Johnson")
                .address("calle 789")
                .email("bob@example.com")
                .username("bobjohnson")
                .password("mypassword")
                .build();

        //When
        UserResponseWithoutMessage result = userMapper.toResponseWithoutMessage(userDTO);

        //Then
        assertThat(result.getId()).isEqualTo(userDTO.getId());
        assertThat(result.getName()).isEqualTo(userDTO.getName());
        assertThat(result.getLastname()).isEqualTo(userDTO.getLastname());
        assertThat(result.getAddress()).isEqualTo(userDTO.getAddress());
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getUsername()).isEqualTo(userDTO.getUsername());
    }

    @Test
    void toResponseWithoutMessage_WithNullUserDTO_ShouldReturnNull() {
        //Given
        UserDTO userDTO = null;

        //When
        UserResponseWithoutMessage result = userMapper.toResponseWithoutMessage(userDTO);

        //Then
        assertThat(result).isNull();
    }

    @Test
    void toResponseWithoutMessage_WithRandomUserDTO_ShouldMapAllFields() {
        //Given
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);

        //When
        UserResponseWithoutMessage result = userMapper.toResponseWithoutMessage(userDTO);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDTO.getId());
        assertThat(result.getName()).isEqualTo(userDTO.getName());
        assertThat(result.getLastname()).isEqualTo(userDTO.getLastname());
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getUsername()).isEqualTo(userDTO.getUsername());
    }

    @Test
    void toDTO_WithPartialUserRequest_ShouldHandleNullFields() {
        //Given
        UserRequest userRequest = UserRequest.builder()
                .userId(1L)
                .name("John")
                .lastname(null)
                .address("")
                .email("john@example.com")
                .username("johndoe")
                .password(null)
                .build();
        //When
        UserDTO result = userMapper.toDTO(userRequest);

        //Then
        assertThat(result.getId()).isEqualTo(userRequest.getUserId());
        assertThat(result.getName()).isEqualTo(userRequest.getName());
        assertThat(result.getLastname()).isNull();
        assertThat(result.getAddress()).isEqualTo("");
        assertThat(result.getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(result.getUsername()).isEqualTo(userRequest.getUsername());
        assertThat(result.getPassword()).isNull();
    }

    @Test
    void toResponse_WithPartialUserDTO_ShouldHandleNullFields() {
        //Given
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .name(null)
                .lastname("Doe")
                .address("")
                .email("john@example.com")
                .username(null)
                .password("password123")
                .build();

        // When
        UserResponse result = userMapper.toResponse(userDTO);

        //Then
        assertThat(result.getId()).isEqualTo(userDTO.getId());
        assertThat(result.getName()).isNull();
        assertThat(result.getLastname()).isEqualTo(userDTO.getLastname());
        assertThat(result.getAddress()).isEqualTo("");
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getUsername()).isNull();
    }

    @Test
    void mappingConsistency_BetweenDifferentMethods_ShouldBeConsistent() {
        //Given
        UserRequest userRequest = easyRandom.nextObject(UserRequest.class);
        UserDTO userDTO = userMapper.toDTO(userRequest);

        //When
        UserResponse userResponse = userMapper.toResponse(userDTO);
        UserResponseWithoutMessage userResponseWithoutMessage = userMapper.toResponseWithoutMessage(userDTO);

        //Then
        assertThat(userResponse.getId()).isEqualTo(userResponseWithoutMessage.getId());
        assertThat(userResponse.getName()).isEqualTo(userResponseWithoutMessage.getName());
        assertThat(userResponse.getLastname()).isEqualTo(userResponseWithoutMessage.getLastname());
        assertThat(userResponse.getEmail()).isEqualTo(userResponseWithoutMessage.getEmail());
        assertThat(userResponse.getUsername()).isEqualTo(userResponseWithoutMessage.getUsername());
    }

    @Test
    void toDTO_WithEmptyStrings_ShouldPreserveEmptyStrings() {
        //Given
        UserRequest userRequest = UserRequest.builder()
                .userId(1L)
                .name("")
                .lastname("")
                .address("")
                .email("")
                .username("")
                .password("")
                .build();

        //When
        UserDTO result = userMapper.toDTO(userRequest);

        //Then
        assertThat(result.getId()).isEqualTo(userRequest.getUserId());
        assertThat(result.getName()).isEqualTo("");
        assertThat(result.getLastname()).isEqualTo("");
        assertThat(result.getAddress()).isEqualTo("");
        assertThat(result.getEmail()).isEqualTo("");
        assertThat(result.getUsername()).isEqualTo("");
        assertThat(result.getPassword()).isEqualTo("");
    }

    @Test
    void toResponse_WithSpecialCharacters_ShouldHandleCorrectly() {
        //Given
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .name("José María")
                .lastname("Gonzáles-Pérez")
                .address("Calle 123 #45-67")
                .email("jose.maria@example.com")
                .username("josé_maría")
                .password("contraseña123!")
                .build();

        //When
        UserResponse result = userMapper.toResponse(userDTO);

        //Then
        assertThat(result.getName()).isEqualTo("José María");
        assertThat(result.getLastname()).isEqualTo("Gonzáles-Pérez");
        assertThat(result.getAddress()).isEqualTo("Calle 123 #45-67");
        assertThat(result.getEmail()).isEqualTo("jose.maria@example.com");
        assertThat(result.getUsername()).isEqualTo("josé_maría");
    }
    
}