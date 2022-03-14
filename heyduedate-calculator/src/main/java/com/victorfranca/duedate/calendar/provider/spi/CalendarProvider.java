package com.victorfranca.duedate.calendar.provider.spi;

import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.provider.spi.exception.CalendarElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.exception.InvalidCalendarException;

/**
 * @author victor.franca
 *
 */
public interface CalendarProvider {

	public Calendar createCalendar() throws CalendarElementNotFound, InvalidCalendarException;

}
