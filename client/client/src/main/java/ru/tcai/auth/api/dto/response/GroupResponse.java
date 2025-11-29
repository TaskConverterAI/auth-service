package ru.tcai.auth.api.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class GroupResponse {

    Long id;
    String name;
    String description;
    Long ownerId;
    Instant createdAt;
    int memberCount;

}