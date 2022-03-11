package com.victorfranca.duedate.calculator.multiday.nonWorkingDays;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;;

public class NonBusinessDaysTest_DifferentLocations_Test {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final int START_HOUR_1 = 3;
	private static final int END_HOUR_1 = 6;

	private static final String LOCATION_ID_2 = "LOCATION_ID_2";
	private static final int START_HOUR_2 = 12;
	private static final int END_HOUR_2 = 18;

	@Before
	public void inid() {
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
	public void calculateDueDateTest_nonBusinessFirstDayFirstBlock_2blocks_05_00_2h() {
		// Given
		LocalDate nbdDate = LocalDate.of(2022, 1, 1);

		calendar.setNonBusinessDaysByLocation(Map.of(LOCATION_ID_1, List.of(nbdDate)));

		// WHen
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 05, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 14, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_nonBusinessFirstDayFirstBlock_2blocks_05_00_420h() {
		// Given
		LocalDate nbdDate = LocalDate.of(2022, 1, 1);

		calendar.setNonBusinessDaysByLocation(Map.of(LOCATION_ID_1, List.of(nbdDate)));

		// When
		int slaInMinutes = 60 * 7;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 05, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 4, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_nonBusinessSecondtDayFirstBlock_2blocks_17_00_2h() {
		// Given
		LocalDate nbdDate = LocalDate.of(2022, 1, 2);

		calendar.setNonBusinessDaysByLocation(Map.of(LOCATION_ID_1, List.of(nbdDate)));

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 17, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 13, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes));
	}

}
