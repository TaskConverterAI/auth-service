package ru.tcai.auth.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.tcai.auth.core.dao.UserDao;
import ru.tcai.auth.core.exception.AuthServiceException;
import ru.tcai.auth.core.validation.UniqueUsername;

@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserDao userDao;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userDao.existsByUsername(username)) {
            throw new AuthServiceException("username is already taken", HttpStatus.CONFLICT);
        }
        return true;
    }
}
