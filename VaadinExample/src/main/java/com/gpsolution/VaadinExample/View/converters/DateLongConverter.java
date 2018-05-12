package com.gpsolution.VaadinExample.View.converters;

import java.time.LocalDate;
import java.util.Date;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class DateLongConverter implements Converter<LocalDate, Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Result<Long> convertToModel(LocalDate localDate, ValueContext context) {
        int year = localDate.getYear() - 1900;
        int month = localDate.getMonthValue() - 1;
        int day = localDate.getDayOfMonth();
        @SuppressWarnings("deprecation")
		Date date = new Date(year,month,day);
        return Result.ok(date.getTime());
	}

	@Override
	public LocalDate convertToPresentation(Long aLong, ValueContext context) {
        if (aLong != null){
            Date date = new Date(aLong);
            @SuppressWarnings("deprecation")
			LocalDate localDate = LocalDate.of(1900 + date.getYear(),1 + date.getMonth(), date.getDate());
            return localDate;
        } else {
            return null;
        }
	}
}
