package com.victorfranca.duedate.calculator.singleday.singleblock;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

public class SingleBlock_StartDateInOrAfterFirstCalendarBlock_Test {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final int START_HOUR_1 = 3;
	private static final int END_HOUR_1 = 6;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";

	@Before
	public void init() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		calendar.setRegularBusinessHours(
				List.of(LocationRegularBusinessHours.builder().location(LOCATION_ID_1).startHour(START_HOUR_1)
						.startMinute(0).endHour(END_HOUR_1).endMinute(0).build()));

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
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));

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
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));

	}

	@Test
	public void calculateDueDateTest_03_01_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 01);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 01),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));

	}

	@Test
	public void calculateDueDateTest_01_59_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 59);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 59),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
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
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_03_01_01m() {
		// When
		int slaInMinutes = 1 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 01);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 3, 02),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));

	}

	@Test
	public void calculateDueDateTest_01_59_01m() {
		// When
		int slaInMinutes = 1 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 59);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
	}

}
