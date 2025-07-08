package com.wingtrip.user.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User response without message field (used for lists)")
public class UserResponseWithoutMessage {

    @Schema(description = "User's unique identifier", example = "1")
    private Long id;

    @Schema(description = "User's first name", example = "Pepito")
    private String name;

    @Schema(description = "User's last name", example = "Perez")
    private String lastname;

    @Schema(description = "User's address", example = "calle falsa 123")
    private String address;

    @Schema(description = "User's email address", example = "pepito@gmail.com")
    private String email;

    @Schema(description = "User's username", example = "pepitoP")
    private String username;
}
