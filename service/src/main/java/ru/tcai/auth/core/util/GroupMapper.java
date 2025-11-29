package ru.tcai.auth.core.util;

import ru.tcai.auth.api.dto.response.*;
import ru.tcai.auth.core.entity.Group;
import ru.tcai.auth.core.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class GroupMapper {

    private GroupMapper() {
    }

    public static GroupResponse toGroupResponse(Group group) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .ownerId(group.getOwnerId())
                .createdAt(group.getCreatedAt())
                .memberCount(group.getMembers().size())
                .build();
    }

    public static GroupSummaryResponse toGroupSummaryResponse(Group group) {
        return GroupSummaryResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .memberCount(group.getMembers().size())
                .createdAt(group.getCreatedAt())
                .build();
    }

    public static GroupDetailsResponse toGroupDetailsResponse(Group group, Long currentUserId) {
        List<GroupMemberResponse> members = group.getMembers().stream()
                .map(GroupMapper::toGroupMemberResponse)
                .collect(Collectors.toList());

        String role = group.getOwnerId().equals(currentUserId) ? "owner" :
                group.getMembers().stream().anyMatch(u -> u.getId().equals(currentUserId)) ? "member" : "guest";

        return GroupDetailsResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .ownerId(group.getOwnerId())
                .createdAt(group.getCreatedAt())
                .members(members)
                .userRole(role)
                .memberNumber(members.size())
                .build();
    }

    public static GroupMemberResponse toGroupMemberResponse(User user) {
        return GroupMemberResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .joinedAt(user.getCreatedAt())
                .build();
    }

    public static OwnershipTransferResponse.MemberInfo toMemberInfo(User user) {
        return OwnershipTransferResponse.MemberInfo.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}