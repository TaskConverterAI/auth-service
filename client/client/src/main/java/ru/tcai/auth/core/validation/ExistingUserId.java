package ru.tcai.auth.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.tcai.auth.core.validation.validator.ExistingUserIdValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ExistingUserIdValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingUserId {

    String message() default "Couldn't find user with such id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}