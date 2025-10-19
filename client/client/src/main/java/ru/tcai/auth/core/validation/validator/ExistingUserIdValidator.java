package ru.tcai.auth.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;
import ru.tcai.auth.core.validation.ExistingUserId;

@ConditionalOnMissingClass("ru.tcai.auth.core.validation.validator.ExistingUserIdValidator")
@Component
@RequiredArgsConstructor
public class ExistingUserIdValidator implements ConstraintValidator<ExistingUserId, Long> {

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        return true;
    }
}
