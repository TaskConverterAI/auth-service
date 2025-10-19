package ru.tcai.auth.core.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tcai.auth.core.entity.Session;
import ru.tcai.auth.core.entity.User;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface SessionDao extends JpaRepository<Session, Long> {

    @Modifying
    @Query("update Session s set s.expiresAt = :newExpiration where s.refreshToken = :refreshToken and s.isActive = true")
    void prolongateSession(@Param("refreshToken") String refreshToken,
                           @Param("newExpiration") Instant newExpiration);

    @EntityGraph(attributePaths = "user")
    Optional<Session> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("update Session s set s.isActive = false where s.user.id = :userId")
    void deactivateByUserId(@Param("userId") Long userId);

    Optional<Session> findByUserAndIsActive(User user, Boolean isActive);
}

