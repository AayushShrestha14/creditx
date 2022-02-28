package com.sb.solutions.core.validation.constraint;

import java.lang.annotation.*;

/**
 * @author : Rujan Maharjan on  12/7/2020
 **/
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SbValid {
    String value();
}
