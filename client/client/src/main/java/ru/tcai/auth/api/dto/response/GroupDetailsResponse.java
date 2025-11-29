package ru.tcai.auth.api.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class GroupDetailsResponse {

    Long id;
    String name;
    String description;
    Long ownerId;
    Instant createdAt;
    String userRole;
    List<GroupMemberResponse> members;
}