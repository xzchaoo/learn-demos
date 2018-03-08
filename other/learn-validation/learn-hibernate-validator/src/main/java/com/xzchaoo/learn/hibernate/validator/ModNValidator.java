package com.xzchaoo.learn.hibernate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ModNValidator implements ConstraintValidator<ModNConstraint, Integer> {
	private int divider;
	private int remain;

	@Override
	public void initialize(ModNConstraint constraintAnnotation) {
		this.divider = constraintAnnotation.divider();
		this.remain = constraintAnnotation.remain();
		if (this.divider <= 0) {
			throw new IllegalArgumentException("n不能小于等于0");
		}
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		return value % divider == remain;
	}
}
