package ru.tcai.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RefreshAccessTokenRequest {

    @NotBlank(message = "Refresh token must not be blank")
    @Size(max = 255, message = "Refresh token must be at most 255 characters")
    String refreshToken;

}
