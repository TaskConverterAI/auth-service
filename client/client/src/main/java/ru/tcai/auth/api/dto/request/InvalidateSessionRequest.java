package ru.tcai.auth.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import ru.tcai.auth.core.validation.ExistingUserId;

@Builder
@Value
public class InvalidateSessionRequest {

    @NotNull(message = "Id can't be null")
    @Positive(message = "Id can't be negative or zero")
    @ExistingUserId
    Long userId;

}
