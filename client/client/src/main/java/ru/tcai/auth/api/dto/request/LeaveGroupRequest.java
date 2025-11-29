package ru.tcai.auth.api.dto.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LeaveGroupRequest {

    Long transferOwnershipTo;
}