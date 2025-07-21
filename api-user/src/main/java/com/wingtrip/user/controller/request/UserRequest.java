package com.wingtrip.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User request for creation and updates")
public class UserRequest {

    @Schema(description = "User ID(only for updates, leave null for creation", example = "1")
    private Long userId;

    @Schema(description = "User first name", example = "Pepito", required = true)
    @NotBlank(message = "The name is required")
    private String name;

    @Schema(description = "User last name", example = "Perez")
    @NotBlank(message = "The lastname is required")
    private String lastname;

    @Schema(description = "User's address", example = "calle falsa 123")
    private String address;

    @Schema(description = "User's email address", example = "pepito@gmail.com")
    @NotBlank(message = "The email is required")
    @Email(message = "The email must be in a valid format")
    private String email;

    @Schema(description = "Unique username", example = "pepitoP")
    @NotBlank(message = "The username is required")
    private String username;

    @Schema(description = "User's password", example = "12345678")
    @NotBlank(message = "The password is required")
    private String password;
}
