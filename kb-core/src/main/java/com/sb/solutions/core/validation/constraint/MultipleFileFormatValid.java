package com.sb.solutions.core.validation.constraint;

import com.sb.solutions.core.validation.validator.MultipleFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Ayonij Karki on 1/12/2022
 */

@Documented
@Constraint(
        validatedBy = {MultipleFileValidator.class}
)
@Target({ElementType.PARAMETER, ElementType.FIELD,ElementType.TYPE,ElementType.METHOD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipleFileFormatValid {
    String message() default "must not be empty";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
