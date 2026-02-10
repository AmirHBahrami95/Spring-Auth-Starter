package com.XX_DOMAIN_NAME.XX_APP_NAME.utils.validator;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinYearsAgoValidator implements 
ConstraintValidator<MinYearsAgo, LocalDate> {
	
	
	private long min;
	
	@Override
    public void initialize(MinYearsAgo dr) {
		min=dr.value();
	}

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		boolean valid=true;
		LocalDate today=LocalDate.now();
		if(min>0) valid = valid && value.isBefore(today.minusYears(min));
		return valid;
	}

}
