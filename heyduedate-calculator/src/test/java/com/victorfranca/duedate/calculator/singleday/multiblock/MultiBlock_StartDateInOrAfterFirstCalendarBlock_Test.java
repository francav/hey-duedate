package com.victorfranca.duedate.calculator.singleday.multiblock;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

/**
 * @author victor.franca
 *
 */
public class MultiBlock_StartDateInOrAfterFirstCalendarBlock_Test {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final int START_HOUR_1 = 3;
	private static final int END_HOUR_1 = 6;

	private static final String LOCATION_ID_2 = "LOCATION_ID_2";
	private static final int START_HOUR_2 = 12;
	private static final int END_HOUR_2 = 18;

	private static final String LOCATION_ID_3 = "LOCATION_ID_3";
	private static final int START_HOUR_3 = 20;
	private static final int END_HOUR_3 = 23;

	@Before
	public void init() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		calendar.setRegularBusinessHours(List.of(

				LocationRegularBusinessHours.builder().location(LOCATION_ID_1).startHour(START_HOUR_1).startMinute(0)
						.endHour(END_HOUR_1).endMinute(0).build(),

				LocationRegularBusinessHours.builder().location(LOCATION_ID_2).startHour(START_HOUR_2).startMinute(0)
						.endHour(END_HOUR_2).endMinute(0).build(),

				LocationRegularBusinessHours.builder().location(LOCATION_ID_3).startHour(START_HOUR_3).startMinute(0)
						.endHour(END_HOUR_3).endMinute(0).build()));

	}

	//////////////////////////////////////
	// SLA: 240 MINUTES - 2 blocks
	/////////////////////////////////////

	@Test
	public void calculateDueDateTest_2blocks_03_00_4h() {
		// When
		int slaInMinutes = 60 * 4;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 13, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_2blocks_03_01_4h() {
		// When
		int slaInMinutes = 60 * 4;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 01);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 13, 01),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_2_blocks_03_59_4h() {
		// When
		int slaInMinutes = 60 * 4;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 59);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 13, 59),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	//////////////////////////////////////
	// SLA: 600 MINUTES - 3 blocks
	/////////////////////////////////////

	@Test
	public void calculateDueDateTest_2blocks_03_00_10h() {
		// When
		int slaInMinutes = 60 * 10;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 21, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

}
