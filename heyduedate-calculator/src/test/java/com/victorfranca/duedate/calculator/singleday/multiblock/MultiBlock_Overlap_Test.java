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
public class MultiBlock_Overlap_Test {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final int START_HOUR_1 = 0;
	private static final int END_HOUR_1 = 5;

	private static final String LOCATION_ID_2 = "LOCATION_ID_2";
	private static final int START_HOUR_2 = 3;
	private static final int END_HOUR_2 = 7;

	@Before
	public void init() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		calendar.setRegularBusinessHours(List.of(

				LocationRegularBusinessHours.builder().location(LOCATION_ID_1).startHour(START_HOUR_1).startMinute(0)
						.endHour(END_HOUR_1).endMinute(0).build(),

				LocationRegularBusinessHours.builder().location(LOCATION_ID_2).startHour(START_HOUR_2).startMinute(0)
						.endHour(END_HOUR_2).endMinute(0).build()));
	}

	@Test
	public void calculateDueDateTest_00_00_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 00, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 1, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_00_00_5h() {
		// When
		int slaInMinutes = 60 * 5;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 00, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 5, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_00_00_7h() {
		// When
		int slaInMinutes = 60 * 7;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 00, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 7, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_00_00_8h() {
		// When
		int slaInMinutes = 60 * 8;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 00, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 1, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

}
