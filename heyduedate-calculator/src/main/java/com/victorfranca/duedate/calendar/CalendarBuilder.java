package com.victorfranca.duedate.calendar;

import com.victorfranca.duedate.calendar.provider.spi.CalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.DayLightSavingProvider;
import com.victorfranca.duedate.calendar.provider.spi.NonBusinessDayProvider;

/**
 * @author victor.franca
 *
 */
public class CalendarBuilder {
	
	private CalendarProvider calendarProvider;
	
	private NonBusinessDayProvider nonBusinessDaysProvider;

	private DayLightSavingProvider dayLightSavingProvider;


}
