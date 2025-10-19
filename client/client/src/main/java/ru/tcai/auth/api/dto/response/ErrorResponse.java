package ru.tcai.auth.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class ErrorResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String error;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> errors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer code;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp = LocalDateTime.now();
}
