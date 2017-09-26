package com.xzchaoo.learn.hibernate.validator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SearchRequestValidator implements ConstraintValidator<ValidSearchRequest, SearchRequest> {
	@Override
	public boolean isValid(SearchRequest value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		LocalDate inboundDate = value.getInboundDate();
		if (inboundDate != null) {
			if (inboundDate.isBefore(value.getOutboundDate())) {
				context.disableDefaultConstraintViolation();
//				context
//					.buildConstraintViolationWithTemplate( "{my.custom.template}" )
//					.addPropertyNode( "passengers" ).addConstraintViolation();
				return false;
			}
		}
		return true;
	}
}
