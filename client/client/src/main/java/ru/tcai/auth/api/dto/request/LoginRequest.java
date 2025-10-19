package ru.tcai.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LoginRequest {

    @NotBlank(message = "Username or email must not be blank")
    @Size(max = 255, message = "Username or email must be at most 255 characters")
    String usernameOrEmail;

    @NotBlank(message = "Password must not be blank")
    @Size(max = 255, message = "Password must be at most 255 characters")
    String password;

}
