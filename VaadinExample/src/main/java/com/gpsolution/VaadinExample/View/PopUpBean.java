package com.gpsolution.VaadinExample.View;

public class PopUpBean<T> {
	private String selectFields;
	private T value;
	
	public PopUpBean() {}
	
	public String getSelectFields() {
		return selectFields;
	}
	public void setSelectFields(String selectFields) {
		this.selectFields = selectFields;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
}
