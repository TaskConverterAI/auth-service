package ru.tcai.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddMemberRequest {

    @NotBlank
    String usernameOrEmail;

}
