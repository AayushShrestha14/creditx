package com.sb.solutions.validation.constraint;

import java.lang.annotation.*;

/**
 * @author : Rujan Maharjan on  12/4/2020
 **/


@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CadValid {


}
