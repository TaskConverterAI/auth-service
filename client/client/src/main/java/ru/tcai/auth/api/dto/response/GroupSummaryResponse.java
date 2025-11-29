package ru.tcai.auth.api.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class GroupSummaryResponse {

    Long id;
    String name;
    String description;
    int memberCount;
    Instant createdAt;
}