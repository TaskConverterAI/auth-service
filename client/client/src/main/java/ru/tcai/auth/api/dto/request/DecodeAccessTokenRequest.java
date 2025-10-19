package ru.tcai.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class DecodeAccessTokenRequest {

    @NotBlank(message = "Access token must not be blank")
    @Size(max = 511, message = "Access token must be at most 255 characters")
    String accessToken;

}
