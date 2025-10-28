package ru.tcai.auth.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.tcai.auth.core.dao.UserDao;
import ru.tcai.auth.core.exception.AuthServiceException;
import ru.tcai.auth.core.validation.ExistingUserId;

@Component
@RequiredArgsConstructor
public class ExistingUserIdValidator implements ConstraintValidator<ExistingUserId, Long> {

    private final UserDao userDao;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        if (!userDao.existsById(userId)) {
            throw new AuthServiceException("user with such id not found", HttpStatus.NOT_FOUND);
        }
        return true;
    }
}
