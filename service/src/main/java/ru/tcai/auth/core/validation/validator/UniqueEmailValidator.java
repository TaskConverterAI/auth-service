package ru.tcai.auth.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tcai.auth.core.dao.UserDao;
import ru.tcai.auth.core.validation.UniqueEmail;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserDao userDao;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userDao.existsByEmail(email);
    }
}
