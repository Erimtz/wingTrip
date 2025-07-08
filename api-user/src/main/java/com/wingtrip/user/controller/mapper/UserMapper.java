package com.wingtrip.user.controller.mapper;

import com.wingtrip.user.controller.request.UserRequest;
import com.wingtrip.user.controller.response.UserResponse;
import com.wingtrip.user.controller.response.UserResponseWithoutMessage;
import com.wingtrip.user.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", source = "userId")
    UserDTO toDTO(UserRequest request);

    UserResponse toResponse(UserDTO userDTO);

    UserResponseWithoutMessage toResponseWithoutMessage(UserDTO userDTO);
}
