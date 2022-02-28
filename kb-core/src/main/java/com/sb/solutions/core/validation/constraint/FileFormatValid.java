package com.sb.solutions.core.validation.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import com.sb.solutions.core.validation.validator.FileValidator;

/**
 * @author Sunil Babu Shrestha
 * 11/2/2021
 */

@Documented
@Constraint(
    validatedBy = {FileValidator.class}
)
@Target({ElementType.PARAMETER, ElementType.FIELD,ElementType.TYPE,ElementType.METHOD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileFormatValid {
    String message() default "must not be empty";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
