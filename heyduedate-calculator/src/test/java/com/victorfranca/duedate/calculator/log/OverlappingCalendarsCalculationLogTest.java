package com.victorfranca.duedate.calculator.log;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

public class OverlappingCalendarsCalculationLogTest {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final String LOCATION_ID_2 = "LOCATION_ID_2";

	@Before
	public void init() {
		// Given
		int startHour1 = 0;
		int endHour1 = 5;

		int startHour2 = 3;
		int endHour2 = 7;

		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		calendar.setRegularBusinessHours(List.of(

				LocationRegularBusinessHours.builder().location(LOCATION_ID_1).startHour(startHour1).startMinute(0)
						.endHour(endHour1).endMinute(0).build(),

				LocationRegularBusinessHours.builder().location(LOCATION_ID_2).startHour(startHour2).startMinute(0)
						.endHour(endHour2).endMinute(0).build()));
	}

	@Test
	public void shouldKeepCalculationLog_SLA1h_Start3h() {

		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		CalculationLog calculationLog = dueDateCalculator.calculateDueDateWithLog(calendar, startDateTime,
				slaInMinutes);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 04, 00), calculationLog.getDueDateTime());

		assertEquals(LocalDateTime.of(2022, 1, 1, 00, 00), calculationLog.get(0).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 03, 00), calculationLog.get(0).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(0).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 1, 03, 00), calculationLog.get(1).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 05, 00), calculationLog.get(1).getEnd());
		assertEquals(Long.valueOf(60), calculationLog.get(1).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 1, 05, 00), calculationLog.get(2).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 07, 00), calculationLog.get(2).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(2).getSlaUsedTimeInMinutes());
	}

	@Test
	public void shouldKeepCalculationLog_SLA2h_Start6h() {

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 6, 00);

		CalculationLog calculationLog = dueDateCalculator.calculateDueDateWithLog(calendar, startDateTime,
				slaInMinutes);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 01, 00), calculationLog.getDueDateTime());

		assertEquals(LocalDateTime.of(2022, 1, 1, 00, 00), calculationLog.get(0).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 03, 00), calculationLog.get(0).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(0).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 1, 03, 00), calculationLog.get(1).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 05, 00), calculationLog.get(1).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(1).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 1, 05, 00), calculationLog.get(2).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 07, 00), calculationLog.get(2).getEnd());
		assertEquals(Long.valueOf(60), calculationLog.get(2).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 2, 00, 00), calculationLog.get(3).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 2, 03, 00), calculationLog.get(3).getEnd());
		assertEquals(Long.valueOf(60), calculationLog.get(3).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 2, 03, 00), calculationLog.get(4).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 2, 05, 00), calculationLog.get(4).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(4).getSlaUsedTimeInMinutes());

		assertEquals(LocalDateTime.of(2022, 1, 2, 05, 00), calculationLog.get(5).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 2, 07, 00), calculationLog.get(5).getEnd());
		assertEquals(Long.valueOf(0), calculationLog.get(5).getSlaUsedTimeInMinutes());
}

}
