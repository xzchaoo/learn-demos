package com.xzchaoo.learn.hibernate.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = ModNValidator.class)
@Documented
@Repeatable(ModNConstraint.List.class)
public @interface ModNConstraint {

	String message() default "{com.xzchaoo.learn.hibernate.validator.ModNConstraint.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int divider();

	int remain();

	@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
	@Retention(RUNTIME)
	@Documented
	@interface List {
		ModNConstraint[] value();
	}
}
