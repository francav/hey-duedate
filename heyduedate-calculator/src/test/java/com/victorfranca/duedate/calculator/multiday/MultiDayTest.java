package com.victorfranca.duedate.calculator.multiday;

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
public class MultiDayTest {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final String LOCATION_ID_2 = "LOCATION_ID_2";

	private static final int START_HOUR_1 = 3;
	private static final int END_HOUR_1 = 6;

	private static final int START_HOUR_2 = 12;
	private static final int END_HOUR_2 = 18;

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
	public void calculateDueDateTest_2blocks_17_00_2h() {

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 17, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 04, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_2blocks_11_00_7h() {
		// When
		int slaInMinutes = 60 * 7;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 11, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 04, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_2blocks_11_00_24h() {
		// When
		int slaInMinutes = 60 * 24;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 11, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 3, 18, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
	}

}
