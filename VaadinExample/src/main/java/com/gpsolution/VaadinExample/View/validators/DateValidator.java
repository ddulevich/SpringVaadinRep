package com.gpsolution.VaadinExample.View.validators;

import java.util.Date;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class DateValidator implements Validator<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ValidationResult apply(Long aLong, ValueContext context) {
        Date date = new Date(aLong + 24*60*60*1000);
        boolean validation = date.before(new Date());
        if (validation){
            return ValidationResult.ok();
        } else {
            return ValidationResult.error("Date must be in the past");
        }
	}

}
