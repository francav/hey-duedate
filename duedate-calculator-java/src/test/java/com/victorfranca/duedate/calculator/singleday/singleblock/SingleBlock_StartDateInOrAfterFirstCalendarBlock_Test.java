package com.victorfranca.duedate.calculator.singleday.singleblock;

import static com.victorfranca.duedate.calculator.CalendarBlockDataBuilder.createCalendarBlock;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.victorfranca.duedate.calculator.CalendarProvider;
import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.CalendarBlock;
import com.victorfranca.duedate.calendar.CalendarDay;

public class SingleBlock_StartDateInOrAfterFirstCalendarBlock_Test {

	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";

	@Before
	public void init() {
		// Given
		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {
			@Override
			public Calendar getCalendar() {

				CalendarDay calendarDay = new CalendarDay();
				CalendarBlock calendarBlock1 = createCalendarBlock(block1Location, block1Start, block1End);

				calendarDay.addCalendarBlock(calendarBlock1);

				return new Calendar(calendarDay);
			}
		});

	}

	//////////////////////////////////////
	// SLA: 240 MINUTES
	/////////////////////////////////////

	@Test
	public void calculateDueDateTest_16_00_4h() {
		// When
		int slaInMinutes = 60 * 4;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 16, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 3, 4, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));

	}

	//////////////////////////////////////
	// SLA: 60 MINUTES
	/////////////////////////////////////

	@Test
	public void calculateDueDateTest_03_00_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));

	}

	@Test
	public void calculateDueDateTest_03_01_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 01);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 01),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));

	}

	@Test
	public void calculateDueDateTest_01_59_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 59);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 59),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	//////////////////////////////////////
	// SLA: 1 MINUTE
	/////////////////////////////////////

	@Test
	public void calculateDueDateTest_03_00_01m() {
		// When
		int slaInMinutes = 1 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 3, 01),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_03_01_01m() {
		// When
		int slaInMinutes = 1 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 01);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 3, 02),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));

	}

	@Test
	public void calculateDueDateTest_01_59_01m() {
		// When
		int slaInMinutes = 1 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 59);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

}
