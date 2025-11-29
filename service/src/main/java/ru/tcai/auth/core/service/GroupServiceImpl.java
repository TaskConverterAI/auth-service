package ru.tcai.auth.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tcai.auth.api.dto.request.AddMemberRequest;
import ru.tcai.auth.api.dto.request.CreateGroupRequest;
import ru.tcai.auth.api.dto.request.LeaveGroupRequest;
import ru.tcai.auth.api.dto.response.GroupDetailsResponse;
import ru.tcai.auth.api.dto.response.GroupMemberResponse;
import ru.tcai.auth.api.dto.response.GroupResponse;
import ru.tcai.auth.api.dto.response.GroupSummaryResponse;
import ru.tcai.auth.api.dto.response.OwnershipTransferResponse;
import ru.tcai.auth.core.dao.GroupDao;
import ru.tcai.auth.core.dao.UserDao;
import ru.tcai.auth.core.entity.Group;
import ru.tcai.auth.core.entity.User;
import ru.tcai.auth.core.exception.AuthServiceException;
import ru.tcai.auth.core.util.GroupMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupDao groupDao;
    private final UserDao userDao;

    @Override
    @Transactional
    public GroupResponse createGroup(CreateGroupRequest request) {
        User owner = userDao.findById(request.getUserId())
                .orElseThrow(() -> new AuthServiceException("User not found", HttpStatus.NOT_FOUND));
        Group group = Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ownerId(owner.getId())
                .members(Set.of(owner))
                .build();
        groupDao.save(group);
        return GroupMapper.toGroupResponse(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupSummaryResponse> getUserGroups(Long userId) {
        return groupDao.findAllByMembers_IdOrOwnerId(userId, userId).stream()
                .map(GroupMapper::toGroupSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDetailsResponse getGroupById(Long groupId) {
        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new AuthServiceException("Group not found", HttpStatus.NOT_FOUND));
        return GroupMapper.toGroupDetailsResponse(group, group.getOwnerId());
    }

    @Override
    @Transactional
    public void deleteGroup(Long groupId, Long userId) {
        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new AuthServiceException("Group not found", HttpStatus.NOT_FOUND));
        if (!group.getOwnerId().equals(userId)) {
            throw new SecurityException("Only owner can delete group");
        }
        groupDao.delete(group);
    }

    @Override
    @Transactional
    public GroupMemberResponse addMember(Long groupId, AddMemberRequest request) {
        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new AuthServiceException("Group not found", HttpStatus.NOT_FOUND));
        User user = userDao.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new AuthServiceException("User not found", HttpStatus.NOT_FOUND));

        if (group.getMembers().contains(user)) {
            throw new AuthServiceException("User already in group", HttpStatus.CONFLICT);
        }

        group.getMembers().add(user);
        groupDao.save(group);

        return GroupMapper.toGroupMemberResponse(user);
    }

    @Override
    @Transactional
    public void removeMember(Long groupId, Long userId) {
        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new AuthServiceException("Group not found", HttpStatus.NOT_FOUND));
        User user = userDao.findById(userId)
                .orElseThrow(() -> new AuthServiceException("User not found", HttpStatus.NOT_FOUND));

        if (!group.getMembers().contains(user)) {
            throw new AuthServiceException("User not in group", HttpStatus.NOT_FOUND);
        }

        if (group.getOwnerId().equals(userId)) {
            throw new AuthServiceException("Owner cannot be removed", HttpStatus.CONFLICT);
        }

        group.getMembers().remove(user);
        groupDao.save(group);
    }

    @Override
    @Transactional
    public OwnershipTransferResponse leaveGroup(Long groupId, LeaveGroupRequest request) {
        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new AuthServiceException("Group not found", HttpStatus.NOT_FOUND));

        User leavingUser = request != null && request.getTransferOwnershipTo() != null ?
                userDao.findById(request.getTransferOwnershipTo())
                        .orElseThrow(() -> new AuthServiceException("Target user not found", HttpStatus.NOT_FOUND)) : null;

        if (group.getOwnerId().equals(leavingUser != null ? leavingUser.getId() : null)) {
            Set<User> eligible = group.getMembers().stream()
                    .filter(u -> !u.getId().equals(group.getOwnerId()))
                    .collect(Collectors.toSet());

            return OwnershipTransferResponse.builder()
                    .message("Ownership transfer required")
                    .eligibleMembers(eligible.stream()
                            .map(GroupMapper::toMemberInfo)
                            .toList())
                    .build();

        } else {
            group.getMembers().removeIf(u -> u.getId().equals(leavingUser != null ? leavingUser.getId() : null));
            groupDao.save(group);
            return OwnershipTransferResponse.builder()
                    .message("Left group successfully")
                    .eligibleMembers(List.of())
                    .build();
        }
    }
}
