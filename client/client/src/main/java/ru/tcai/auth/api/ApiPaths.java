package ru.tcai.auth.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPaths {

    public static final String ROOT_AUTH = "/auth";

    public static final String REGISTER = ROOT_AUTH + "/register";
    public static final String LOGIN = ROOT_AUTH + "/login";
    public static final String REFRESH = ROOT_AUTH + "/refresh";
    public static final String LOGOUT = ROOT_AUTH + "/logout";
    public static final String DECODE = ROOT_AUTH + "/decode";

    public static final String ROOT_GROUPS = "/groups";

    public static final String CREATE_GROUP = ROOT_GROUPS;
    public static final String GET_GROUPS = ROOT_GROUPS;
    public static final String GET_GROUP_BY_ID = ROOT_GROUPS + "/{groupId}";
    public static final String DELETE_GROUP = ROOT_GROUPS + "/{groupId}";

    public static final String ADD_MEMBER = ROOT_GROUPS + "/{groupId}/members";
    public static final String REMOVE_MEMBER = ROOT_GROUPS + "/{groupId}/members/{userId}";
    public static final String LEAVE_GROUP = ROOT_GROUPS + "/{groupId}/leave";
}
