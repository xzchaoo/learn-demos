package com.xzchaoo.learn.hibernate.validator;

import org.hibernate.validator.HibernateValidator;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

public class HibernateValidatorTest {
	@Test
	public void test() {
//		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();

		ValidatorFactory vf = Validation.byProvider(HibernateValidator.class)
			.configure()
			//.failFast(true)
			.buildValidatorFactory();

		Validator v = vf.getValidator();
		SearchRequest sr = new SearchRequest();
		sr.setOutboundDate(LocalDate.now());
		sr.setInboundDate(LocalDate.now().minusDays(1));
		sr.setDepartCityCodes(Arrays.asList("SHA", "BJS", "TYO"));
		sr.setArriveCityCodes(Arrays.asList("HKG"));
		FlightInfo fi = new FlightInfo();
		fi.setCarrier("MU");
		fi.setNumber(123);
		sr.setFlightInfo(fi);
		sr.setGender(null);
		Set<ConstraintViolation<SearchRequest>> set = v.validate(sr, Default.class);
		for (ConstraintViolation<SearchRequest> cv : set) {
			System.out.println(cv.getPropertyPath() + "=" + cv.getInvalidValue() + " : " + cv.getMessage());
		}
	}
}
