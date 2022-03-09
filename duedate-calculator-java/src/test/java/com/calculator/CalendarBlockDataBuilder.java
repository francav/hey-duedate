package com.calculator;

import java.time.LocalDateTime;

import com.calendar.CalendarBlock;

public final class CalendarBlockDataBuilder {

	public static CalendarBlock createCalendarBlock(String location, LocalDateTime start, LocalDateTime end) {
		return new CalendarBlock(location, start, end);
	}

}
