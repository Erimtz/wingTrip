package com.wingtrip.user.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response returned after successfully deleting a user")
public class DeleteResponse {

    @Schema(description = "Confirmation message", example = "Usuario eliminado correctamente")
    private String message;

    @Schema(description = "ID of the deleted user", example = "123")
    private Long userId;
}
