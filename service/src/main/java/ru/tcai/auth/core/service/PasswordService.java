package ru.tcai.auth.core.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public String encode(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean matches(String password, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
        return result.verified;
    }
}
