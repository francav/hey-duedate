package com.victorfranca.duedate.calculator;

import java.time.LocalDateTime;

import com.victorfranca.duedate.calendar.CalendarBlock;

public final class CalendarBlockDataBuilder {

	public static CalendarBlock createCalendarBlock(String location, LocalDateTime start, LocalDateTime end) {
		return new CalendarBlock(location, start, end);
	}

}
