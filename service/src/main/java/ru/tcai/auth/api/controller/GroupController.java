package ru.tcai.auth.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tcai.auth.api.ApiPaths;
import ru.tcai.auth.api.dto.request.AddMemberRequest;
import ru.tcai.auth.api.dto.request.CreateGroupRequest;
import ru.tcai.auth.api.dto.request.LeaveGroupRequest;
import ru.tcai.auth.api.dto.response.GroupDetailsResponse;
import ru.tcai.auth.api.dto.response.GroupMemberResponse;
import ru.tcai.auth.api.dto.response.GroupResponse;
import ru.tcai.auth.api.dto.response.GroupSummaryResponse;
import ru.tcai.auth.api.dto.response.OwnershipTransferResponse;
import ru.tcai.auth.core.service.GroupService;
import ru.tcai.auth.core.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    @PostMapping(ApiPaths.CREATE_GROUP)
    public ResponseEntity<GroupResponse> createGroup(@RequestBody CreateGroupRequest request) {
        GroupResponse response = groupService.createGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(ApiPaths.GET_GROUPS)
    public ResponseEntity<List<GroupSummaryResponse>> getUserGroups(@RequestParam Long userId) {
        List<GroupSummaryResponse> response = groupService.getUserGroups(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiPaths.GET_GROUP_BY_ID)
    public ResponseEntity<GroupDetailsResponse> getGroupById(@PathVariable Long groupId) {
        GroupDetailsResponse response = groupService.getGroupById(groupId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(ApiPaths.DELETE_GROUP)
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId,
                                            @RequestParam Long userId) {
        groupService.deleteGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiPaths.ADD_MEMBER)
    public ResponseEntity<GroupMemberResponse> addMember(@PathVariable Long groupId,
                                                         @RequestBody AddMemberRequest request) {
        GroupMemberResponse response = groupService.addMember(groupId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(ApiPaths.REMOVE_MEMBER)
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId,
                                             @PathVariable Long userId) {
        groupService.removeMember(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiPaths.LEAVE_GROUP)
    public ResponseEntity<OwnershipTransferResponse> leaveGroup(@PathVariable Long groupId,
                                                                @RequestBody(required = false) LeaveGroupRequest request) {
        OwnershipTransferResponse response = groupService.leaveGroup(groupId, request);
        return response == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(response);
    }

    @PostMapping(ApiPaths.GET_MEMBER)
    public ResponseEntity<GroupMemberResponse> getMemberInfo(@PathVariable Long memberId) {
        GroupMemberResponse response = userService.getUser(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
