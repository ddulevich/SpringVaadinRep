package com.gpsolution.VaadinExample.View.converters;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class RatingConverter implements Converter<String, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Result<Integer> convertToModel(String s, ValueContext context) {
        if (s != null){
            return Result.ok(new Integer(s));
        } else {
            return null;
        }
	}

	@Override
	public String convertToPresentation(Integer integer, ValueContext context) {
        if (integer != null){
            return integer.toString();
        } else {
            return "";
        }
	}

}
