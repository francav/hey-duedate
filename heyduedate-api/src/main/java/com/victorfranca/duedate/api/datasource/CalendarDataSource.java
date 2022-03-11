package com.victorfranca.duedate.api.datasource;

import com.victorfranca.duedate.calendar.Calendar;

public interface CalendarDataSource {

	public Calendar getCalendarData() throws CalendarDataSourceException;

}
