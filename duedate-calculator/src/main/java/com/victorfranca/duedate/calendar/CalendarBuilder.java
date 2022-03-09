package com.victorfranca.duedate.calendar;

import com.victorfranca.duedate.calendar.provider.CalendarProvider;
import com.victorfranca.duedate.calendar.provider.DayLightSavingProvider;
import com.victorfranca.duedate.calendar.provider.NonBusinessDayProvider;

public class CalendarBuilder {
	
	private CalendarProvider calendarProvider;
	
	private NonBusinessDayProvider nonBusinessDaysProvider;

	private DayLightSavingProvider dayLightSavingProvider;


}
