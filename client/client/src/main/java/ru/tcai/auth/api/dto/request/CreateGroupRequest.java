package ru.tcai.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateGroupRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    String name;

    @Size(max = 500)
    String description;

    @NotNull
    Long userId;
}