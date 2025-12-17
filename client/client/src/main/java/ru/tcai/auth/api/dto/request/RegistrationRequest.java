package ru.tcai.auth.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import ru.tcai.auth.core.validation.UniqueEmail;
import ru.tcai.auth.core.validation.UniqueUsername;

@Builder
@Value
public class RegistrationRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 32, message = "Username must be 3-32 characters")
    @UniqueUsername
    String username;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must be at most 255 characters")
    @UniqueEmail
    String email;

    @NotBlank(message = "Password must not be blank")
    @Size(max = 255, message = "Password must be at most 255 characters")
    String password;
}
