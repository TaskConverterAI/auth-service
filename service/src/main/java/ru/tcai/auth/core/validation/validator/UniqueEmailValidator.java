package ru.tcai.auth.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.tcai.auth.core.dao.UserDao;
import ru.tcai.auth.core.exception.AuthServiceException;
import ru.tcai.auth.core.validation.UniqueEmail;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserDao userDao;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (userDao.existsByEmail(email)) {
            throw new AuthServiceException("email is already taken", HttpStatus.CONFLICT);
        }
        return true;
    }
}
