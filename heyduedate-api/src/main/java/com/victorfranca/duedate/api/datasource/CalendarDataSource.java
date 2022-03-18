package com.victorfranca.duedate.api.datasource;

import java.util.List;

import com.victorfranca.duedate.calendar.Calendar;

public interface CalendarDataSource {

	public List<String> getCalendars() throws CalendarDataSourceException;

	public Calendar getCalendarData(String calendar) throws CalendarDataSourceException;

}
