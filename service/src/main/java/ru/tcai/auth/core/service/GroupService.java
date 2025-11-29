package ru.tcai.auth.core.service;

import ru.tcai.auth.api.dto.request.AddMemberRequest;
import ru.tcai.auth.api.dto.request.CreateGroupRequest;
import ru.tcai.auth.api.dto.request.LeaveGroupRequest;
import ru.tcai.auth.api.dto.response.GroupDetailsResponse;
import ru.tcai.auth.api.dto.response.GroupMemberResponse;
import ru.tcai.auth.api.dto.response.GroupResponse;
import ru.tcai.auth.api.dto.response.GroupSummaryResponse;
import ru.tcai.auth.api.dto.response.OwnershipTransferResponse;

import java.util.List;

public interface GroupService {

    GroupResponse createGroup(CreateGroupRequest request);

    List<GroupSummaryResponse> getUserGroups(Long userId);

    GroupDetailsResponse getGroupById(Long groupId);

    void deleteGroup(Long groupId, Long userId);

    GroupMemberResponse addMember(Long groupId, AddMemberRequest request);

    void removeMember(Long groupId, Long userId);

    OwnershipTransferResponse leaveGroup(Long groupId, LeaveGroupRequest request);
}