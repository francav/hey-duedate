package com.victorfranca.duedate.calculator.log;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

public class CalculationLogTest {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final String LOCATION_ID_2 = "LOCATION_ID_2";
	private static final String LOCATION_ID_3 = "LOCATION_ID_3";

	@Test
	public void shouldKeepCalculationLog_2blocksSla2h() {

		// Given
		int startHour1 = 3;
		int endHour1 = 6;

		int startHour2 = 12;
		int endHour2 = 18;

		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		calendar.setRegularBusinessHours(List.of(

				LocationRegularBusinessHours.builder().location(LOCATION_ID_1).startHour(startHour1).startMinute(0)
						.endHour(endHour1).endMinute(0).build(),

				LocationRegularBusinessHours.builder().location(LOCATION_ID_2).startHour(startHour2).startMinute(0)
						.endHour(endHour2).endMinute(0).build()));

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 17, 00);

		CalculationLog calculationLog = dueDateCalculator.calculateDueDateWithLog(calendar, startDateTime,
				slaInMinutes);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 04, 00), calculationLog.getDueDateTime());

		assertEquals(LocalDateTime.of(2022, 1, 1, 03, 00), calculationLog.get(0).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 06, 00), calculationLog.get(0).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(0).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 1, 12, 00), calculationLog.get(1).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 18, 00), calculationLog.get(1).getEnd());
		assertEquals(Long.valueOf(60), calculationLog.get(1).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 2, 03, 00), calculationLog.get(2).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 2, 06, 00), calculationLog.get(2).getEnd());
		assertEquals(Long.valueOf(60), calculationLog.get(2).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 2, 12, 00), calculationLog.get(3).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 2, 18, 00), calculationLog.get(3).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(3).getSlaUsedTimeInMinutes());

	}

	@Test
	public void shouldKeepCalculationLog_3blocksSla1h() {

		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		int startHour1 = 1;
		int endHour1 = 5;

		int startHour2 = 6;
		int endHour2 = 9;

		int startHour3 = 13;
		int endHour3 = 23;
		int endMinute3 = 30;

		calendar.setRegularBusinessHours(List.of(

				LocationRegularBusinessHours.builder().location(LOCATION_ID_1).startHour(startHour1).startMinute(0)
						.endHour(endHour1).endMinute(0).build(),

				LocationRegularBusinessHours.builder().location(LOCATION_ID_2).startHour(startHour2).startMinute(0)
						.endHour(endHour2).endMinute(0).build(),

				LocationRegularBusinessHours.builder().location(LOCATION_ID_3).startHour(startHour3).startMinute(0)
						.endHour(endHour3).endMinute(endMinute3).build()));

		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 2, 00);

		CalculationLog calculationLog = dueDateCalculator.calculateDueDateWithLog(calendar, startDateTime,
				slaInMinutes);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 03, 00), calculationLog.getDueDateTime());

		assertEquals(LocalDateTime.of(2022, 1, 1, 01, 00), calculationLog.get(0).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 05, 00), calculationLog.get(0).getEnd());
		assertEquals(Long.valueOf(60), calculationLog.get(0).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 1, 6, 00), calculationLog.get(1).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 9, 00), calculationLog.get(1).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(1).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 1, 13, 00), calculationLog.get(2).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 23, 30), calculationLog.get(2).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(2).getSlaUsedTimeInMinutes());

	}

}
