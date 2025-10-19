package ru.tcai.auth.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;
import ru.tcai.auth.core.validation.UniqueEmail;

@ConditionalOnMissingClass("ru.tcai.auth.core.validation.validator.UniqueEmailValidator")
@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return true;
    }
}