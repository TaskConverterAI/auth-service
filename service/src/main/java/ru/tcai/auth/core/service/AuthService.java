package ru.tcai.auth.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.tcai.auth.api.dto.request.LoginRequest;
import ru.tcai.auth.api.dto.request.RefreshAccessTokenRequest;
import ru.tcai.auth.api.dto.response.TokenResponse;
import ru.tcai.auth.core.dao.SessionDao;
import ru.tcai.auth.core.dao.UserDao;
import ru.tcai.auth.core.entity.Session;
import ru.tcai.auth.core.entity.User;
import ru.tcai.auth.core.exception.AuthServiceException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserDao userDao;

    private final SessionDao sessionDao;

    private final PasswordService passwordService;

    private final JwtService jwtService;

    private final SessionService sessionService;

    public TokenResponse authenticate(LoginRequest loginRequest) {
        User user = userDao.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
                .filter(u -> passwordService.matches(loginRequest.getPassword(), u.getPassword()))
                .orElseThrow(() -> new AuthServiceException("Bad username/email or password", HttpStatus.UNAUTHORIZED));

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = sessionDao.findByUserAndIsActive(user, true)
                .map(Session::getRefreshToken)
                .orElseGet(() -> sessionService.createSession(user));

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public TokenResponse refreshToken(RefreshAccessTokenRequest request) {
        sessionService.isSessionActive(request.getRefreshToken());

        sessionService.prolongateSession(request.getRefreshToken());

        User user = sessionDao.findByRefreshToken(request.getRefreshToken())
                .map(Session::getUser)
                .orElseThrow(() -> new AuthServiceException("Couldn't find session with such refresh token", HttpStatus.UNAUTHORIZED));

        String accessToken = jwtService.generateAccessToken(user);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(request.getRefreshToken()).build();
    }

}
