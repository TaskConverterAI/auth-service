package ru.tcai.auth.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tcai.auth.core.dao.SessionDao;
import ru.tcai.auth.core.entity.Session;
import ru.tcai.auth.core.entity.User;
import ru.tcai.auth.core.exception.AuthServiceException;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SessionService {

    private final SessionDao sessionDao;

    @Value("${app.jwt.refresh-token-expiration}")
    private Duration tokenExpiration;

    @Transactional
    public String createSession(User user) {
        String refreshToken = UUID.randomUUID().toString().toLowerCase(Locale.ENGLISH);

        Session session = new Session();

        session.setUser(user);
        session.setRefreshToken(refreshToken);

        sessionDao.save(session);

        prolongateSession(refreshToken);

        return refreshToken;
    }

    @Transactional
    public void prolongateSession(String refreshToken) {
        sessionDao.prolongateSession(refreshToken, Instant.now().plus(tokenExpiration));
    }

    @Transactional
    public void invalidateSession(Long userId) {
        sessionDao.deactivateByUserId(userId);
    }

    @Transactional(readOnly = true)
    public void isSessionActive(String refreshToken) {
        Session session = sessionDao.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthServiceException("Couldn't find session with such refresh token", HttpStatus.NOT_FOUND));

        if (session.getExpiresAt().isBefore(Instant.now())) {
            throw new AuthServiceException("Session is expired", HttpStatus.UNAUTHORIZED);
        }

        if (!session.getIsActive()) {
            throw new AuthServiceException("Session is not active", HttpStatus.UNAUTHORIZED);
        }
    }

}
