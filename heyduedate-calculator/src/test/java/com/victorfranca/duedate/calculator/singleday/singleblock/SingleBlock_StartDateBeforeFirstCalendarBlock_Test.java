package com.victorfranca.duedate.calculator.singleday.singleblock;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

public class SingleBlock_StartDateBeforeFirstCalendarBlock_Test {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";

	private static final int START_HOUR_1 = 3;
	private static final int END_HOUR_1 = 6;

	@Before
	public void inid() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		calendar.setRegularBusinessHours(
				List.of(LocationRegularBusinessHours.builder().location(LOCATION_ID_1).startHour(START_HOUR_1)
						.startMinute(0).endHour(END_HOUR_1).endMinute(0).build()));

	}

	@Test
	public void calculateDueDateTest_01_00_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 1, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));

	}

	@Test
	public void calculateDueDateTest_01_01_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 1, 01);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
	}

}
