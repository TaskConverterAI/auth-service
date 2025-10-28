package ru.tcai.auth.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tcai.auth.api.ApiPaths;
import ru.tcai.auth.api.dto.request.DecodeAccessTokenRequest;
import ru.tcai.auth.api.dto.request.InvalidateSessionRequest;
import ru.tcai.auth.api.dto.request.LoginRequest;
import ru.tcai.auth.api.dto.request.RefreshAccessTokenRequest;
import ru.tcai.auth.api.dto.request.RegistrationRequest;
import ru.tcai.auth.api.dto.response.AccessTokenResponse;
import ru.tcai.auth.api.dto.response.DecodedTokenResponse;
import ru.tcai.auth.api.dto.response.TokensResponse;
import ru.tcai.auth.core.service.AuthService;
import ru.tcai.auth.core.service.JwtService;
import ru.tcai.auth.core.service.SessionService;
import ru.tcai.auth.core.service.UserService;

@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    private final JwtService jwtService;

    private final SessionService sessionService;

    @PostMapping(ApiPaths.REGISTER)
    public ResponseEntity<TokensResponse> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        userService.registerUser(registrationRequest);
        return ResponseEntity.status(201).body(authService.authenticate(registrationRequest));
    }

    @PostMapping(ApiPaths.LOGIN)
    public ResponseEntity<TokensResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

    @PostMapping(ApiPaths.REFRESH)
    public ResponseEntity<AccessTokenResponse> refreshToken(@RequestBody @Valid RefreshAccessTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping(ApiPaths.LOGOUT)
    public ResponseEntity<Void> invalidateSession(@RequestBody @Valid InvalidateSessionRequest request) {
        sessionService.invalidateSession(request.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiPaths.DECODE)
    public ResponseEntity<DecodedTokenResponse> decodeToken(@RequestBody @Valid DecodeAccessTokenRequest request) {
        return ResponseEntity.ok(jwtService.decodeAccessToken(request.getAccessToken()));
    }

}
