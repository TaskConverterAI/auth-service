package ru.tcai.auth.api.dto.response;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TokensResponse {

    String accessToken;

    String refreshToken;

}
