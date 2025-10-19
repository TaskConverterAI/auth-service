package ru.tcai.auth.core.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tcai.auth.api.dto.response.DecodedTokenResponse;
import ru.tcai.auth.core.entity.User;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${app.jwt.secret-key}")
    private String secretKey;

    @Value("${app.jwt.leeway}")
    private Long leeway;

    @Value("${app.jwt.access-token-expiration}")
    private Duration tokenExpiration;

    private Algorithm algorithm;

    @PostConstruct
    private void init() {
        algorithm = Algorithm.HMAC512(secretKey);
    }

    public String generateAccessToken(User user) {
        Instant expiresAt = Instant.now().plus(tokenExpiration);

        return JWT.create()
                .withSubject("TaskConverterAI")
                .withIssuer("TaskConverterAI-auth-server")
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public DecodedTokenResponse decodeAccessToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).acceptExpiresAt(leeway).build();

        DecodedJWT decodedJwt = verifier.verify(token);

        return DecodedTokenResponse.builder()
                .userId(decodedJwt.getClaim("id").asLong())
                .role(decodedJwt.getClaim("role").asString())
                .build();
    }
}
