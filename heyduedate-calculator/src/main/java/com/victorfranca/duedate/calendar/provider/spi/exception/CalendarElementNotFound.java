package com.victorfranca.duedate.calendar.provider.spi.exception;

public class CalendarElementNotFound extends Exception {

	private static final long serialVersionUID = 1L;

	private String elementName;

	public CalendarElementNotFound(String elementName) {
		this.elementName = elementName;
	}

	@Override
	public String getMessage() {
		return "Calendar Element not found: " + getElementName();
	}
	
	public String getElementName() {
		return elementName;
	}
}
