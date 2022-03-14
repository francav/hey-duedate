package com.victorfranca.duedate.calendar.provider.spi;

import com.victorfranca.duedate.calendar.Calendar;

/**
 * @author victor.franca
 *
 */
public interface CalendarProvider {

	public Calendar createCalendar() throws CalendarDataSourceElementNotFound, InvalidCalendarDataSourceException;

}
