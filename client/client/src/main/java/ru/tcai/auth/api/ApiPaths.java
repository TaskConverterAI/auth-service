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
}
