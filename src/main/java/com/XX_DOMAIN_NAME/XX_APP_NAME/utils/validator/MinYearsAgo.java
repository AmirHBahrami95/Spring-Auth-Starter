package com.XX_DOMAIN_NAME.XX_APP_NAME.utils.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * A constraint to give a  
 * */
@Documented
@Constraint(validatedBy = MinYearsAgoValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinYearsAgo {
	String message() default "err.format.date.out_of_range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    public long value() default 18;
}
