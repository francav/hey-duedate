package com.victorfranca.duedate.calculator.singleday.singleblock;

import static com.victorfranca.duedate.calculator.CalendarBlockDataBuilder.createCalendarBlock;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.victorfranca.duedate.calculator.CalendarProvider;
import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.CalendarDay;

public class SingleBlock_StartDateBeforeFirstCalendarBlock_Test {

	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";

	@Before
	public void inid() {
		//Given
		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {
			@Override
			public Calendar getCalendar() {
				CalendarDay calendarDay = new CalendarDay();

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End));

				return new Calendar(calendarDay);
			}
		});
	}

	@Test
	public void calculateDueDateTest_01_00_1h() {
		//When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 1, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));

	}

	@Test
	public void calculateDueDateTest_01_01_1h() {
		//When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 1, 01);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

}
