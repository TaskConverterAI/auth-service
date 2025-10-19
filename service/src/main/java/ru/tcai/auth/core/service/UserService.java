package ru.tcai.auth.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tcai.auth.api.dto.request.RegistrationRequest;
import ru.tcai.auth.core.dao.UserDao;
import ru.tcai.auth.core.entity.User;

@RequiredArgsConstructor
@Service
public class UserService {

    public static final String USER_ROLE = "USER_ROLE";

    private final UserDao userDao;

    private final PasswordService passwordService;

    @Transactional
    public void registerUser(RegistrationRequest registrationRequest) {
        User user = new User();

        user.setRole(USER_ROLE);
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordService.encode(registrationRequest.getPassword()));

        userDao.save(user);
    }
}
