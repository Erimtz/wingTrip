package com.wingtrip.user.dto;


import com.wingtrip.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String lastname;
    private String address;
    private String email;
    private String username;
    private String password;

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getUserId();
        this.name = userEntity.getName();
        this.lastname = userEntity.getLastname();
        this.address = userEntity.getAddress();
        this.email = userEntity.getEmail();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
    }
}
