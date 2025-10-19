package ru.tcai.auth.api.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import ru.tcai.auth.api.dto.request.DecodeAccessTokenRequest;
import ru.tcai.auth.api.dto.request.InvalidateSessionRequest;
import ru.tcai.auth.api.dto.request.LoginRequest;
import ru.tcai.auth.api.dto.request.RefreshAccessTokenRequest;
import ru.tcai.auth.api.dto.request.RegistrationRequest;
import ru.tcai.auth.api.dto.response.DecodedTokenResponse;
import ru.tcai.auth.api.dto.response.TokenResponse;

import static ru.tcai.auth.api.ApiPaths.DECODE;
import static ru.tcai.auth.api.ApiPaths.LOGIN;
import static ru.tcai.auth.api.ApiPaths.LOGOUT;
import static ru.tcai.auth.api.ApiPaths.REFRESH;
import static ru.tcai.auth.api.ApiPaths.REGISTER;
import static ru.tcai.auth.api.ApiPaths.ROOT_AUTH;

public interface AuthRestClient {

    @PostExchange(ROOT_AUTH + REGISTER)
    void register(@RequestBody RegistrationRequest request);

    @PostExchange(ROOT_AUTH + LOGIN)
    TokenResponse login(@RequestBody LoginRequest loginRequest);

    @PostExchange(ROOT_AUTH + REFRESH)
    TokenResponse refresh(@RequestBody RefreshAccessTokenRequest request);

    @PostExchange(ROOT_AUTH + LOGOUT)
    void logout(@RequestBody InvalidateSessionRequest request);

    @PostExchange(ROOT_AUTH + DECODE)
    DecodedTokenResponse decode(@RequestBody DecodeAccessTokenRequest request);

}