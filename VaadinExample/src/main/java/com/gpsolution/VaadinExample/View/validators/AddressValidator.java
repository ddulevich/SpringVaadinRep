package com.gpsolution.VaadinExample.View.validators;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class AddressValidator implements Validator<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ValidationResult apply(String s, ValueContext context) {
        if (s.length() >= 5) {
            return ValidationResult.ok();
        } else {
            return ValidationResult.error("The address must be at least 5 characters");
        }
	}

}
