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
@Schema(description = "Response structure for existence checks (email, username, etc.")
public class ExistenceResponse {

    @Schema(description = "Indicates whether the value exists", example = "true")
    private boolean exists;

    @Schema(description = "Human-readable message about the result", example = "Email already exists")
    private String message;

    @Schema(description = "The value that was checked", example = "example@wingtrip.com")
    private String value;
}
