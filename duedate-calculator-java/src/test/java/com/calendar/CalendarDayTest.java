package com.calendar;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import com.calendar.CalendarBlock;
import com.calendar.CalendarDay;

//TODO scenarios with SECONDS
//TODO scenarios with non business hours and DST
public class CalendarDayTest {

	private CalendarDay calendarDay;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final String LOCATION_ID_2 = "LOCATION_ID_2";

	@Test
	public void getAdaptedOnDurationInMinutesTest_startDateInSecondBlock() {

		calendarDay = new CalendarDay();

		CalendarBlock calendarBlock1 = new CalendarBlock(LOCATION_ID_1, LocalDateTime.of(2022, 1, 1, 3, 0),
				LocalDateTime.of(2022, 1, 1, 6, 0));
		CalendarBlock calendarBlock2 = new CalendarBlock(LOCATION_ID_2, LocalDateTime.of(2022, 1, 1, 12, 0),
				LocalDateTime.of(2022, 1, 1, 18, 0));

		calendarDay.addCalendarBlock(calendarBlock1).addCalendarBlock(calendarBlock2);

		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 17, 00);

		assertEquals(60, calendarDay.getAdaptedOnDurationInMinutes(startDateTime));

	}

	@Test
	public void getAdaptedOnDurationInMinutesTest_startDateBetweenBlocks() {
		CalendarBlock calendarBlock1 = new CalendarBlock(LOCATION_ID_1, LocalDateTime.of(2022, 1, 1, 3, 0),
				LocalDateTime.of(2022, 1, 1, 6, 0));
		CalendarBlock calendarBlock2 = new CalendarBlock(LOCATION_ID_2, LocalDateTime.of(2022, 1, 1, 12, 0),
				LocalDateTime.of(2022, 1, 1, 18, 0));

		calendarDay = new CalendarDay();
		calendarDay.addCalendarBlock(calendarBlock1).addCalendarBlock(calendarBlock2);

		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 11, 00);

		assertEquals(360, calendarDay.getAdaptedOnDurationInMinutes(startDateTime));
	}

}
