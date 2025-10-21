package ru.tcai.auth.api.dto.response;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AccessTokenResponse {

    String accessToken;

}
