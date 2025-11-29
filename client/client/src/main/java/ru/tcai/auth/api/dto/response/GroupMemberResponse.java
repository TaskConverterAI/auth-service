package ru.tcai.auth.api.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class GroupMemberResponse {

    Long userId;
    String username;
    String email;
    String role;
    Instant joinedAt;
}