package ru.tcai.auth.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tcai.auth.core.dao.UserDao;
import ru.tcai.auth.core.validation.ExistingUserId;

@Component
@RequiredArgsConstructor
public class ExistingUserIdValidator implements ConstraintValidator<ExistingUserId, Long> {

    private final UserDao userDao;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        return userDao.existsById(userId);
    }
}
