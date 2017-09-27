package com.xzchaoo.learn.hibernate.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {SearchRequestValidator.class})
@Documented
public @interface ValidSearchRequest {

	String message() default "{com.xzchaoo.learn.hibernate.validator.ValidSearchRequest}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
