package ru.tcai.auth.api.dto.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OwnershipTransferResponse {

    String message;
    List<MemberInfo> eligibleMembers;

    @Value
    @Builder
    public static class MemberInfo {
        Long userId;
        String username;
        String email;
    }
}