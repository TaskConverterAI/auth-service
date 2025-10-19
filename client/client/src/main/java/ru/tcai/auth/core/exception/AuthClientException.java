package ru.tcai.auth.core.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.tcai.auth.api.dto.response.ErrorResponse;

@RequiredArgsConstructor
@Getter
public class AuthClientException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public AuthClientException(String message) {
        super(message);
        this.errorResponse = null;
    }

}
