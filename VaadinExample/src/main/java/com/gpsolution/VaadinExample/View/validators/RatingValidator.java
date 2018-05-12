package com.gpsolution.VaadinExample.View.validators;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class RatingValidator implements Validator<Integer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ValidationResult apply(Integer integer, ValueContext context) {
        if (integer < 0 || integer > 5){
            return ValidationResult.error("Rating should be from 0 to 5");
        }
        return ValidationResult.ok();
	}

}
