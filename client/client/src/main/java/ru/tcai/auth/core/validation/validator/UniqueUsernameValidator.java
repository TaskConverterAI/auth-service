package ru.tcai.auth.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;
import ru.tcai.auth.core.validation.UniqueUsername;

@ConditionalOnMissingClass("ru.tcai.auth.core.validation.validator.UniqueUsernameValidator")
@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return true;
    }
}
