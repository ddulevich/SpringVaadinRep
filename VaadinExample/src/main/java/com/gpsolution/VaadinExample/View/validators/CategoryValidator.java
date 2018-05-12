package com.gpsolution.VaadinExample.View.validators;

import com.gpsolution.VaadinExample.Entity.Category;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class CategoryValidator implements Validator<Category>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ValidationResult apply(Category value, ValueContext context) {
		if (value == null) {
			return ValidationResult.error("The category is required");
		} else {
			return ValidationResult.ok();	
		}
	}

}
