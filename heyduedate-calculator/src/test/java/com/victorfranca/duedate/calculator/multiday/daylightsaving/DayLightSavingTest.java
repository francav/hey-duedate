package com.victorfranca.duedate.calculator.multiday.daylightsaving;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calculator.daylightsaving.DayLightSavingInfo;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

//TODO Mix DST and no-DST in different locations
/**
 * @author victor.franca
 *
 */
public class DayLightSavingTest {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final int START_HOUR_1 = 3;
	private static final int END_HOUR_1 = 6;

	@Test
	public void calculateDueDateTest_DSTStartsAfter_2h() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 1, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 2, 2, 0);
		int dstOffset = 60;

		calendar.setRegularBusinessHours(List.of(LocationRegularBusinessHours.builder().location(LOCATION_ID_1)
				.startHour(START_HOUR_1).startMinute(0).endHour(END_HOUR_1).endMinute(0).build()));

		calendar.setDayLightSavingInfoByLocation(Map.of(LOCATION_ID_1,
				List.of(DayLightSavingInfo.builder().start(dstStart).end(dstEnd).offsetInMinutes(dstOffset).build())));

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 00, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 06, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_DSTBefore_2h() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 1, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 2, 2, 0);
		int dstOffset = 60;

		calendar.setRegularBusinessHours(List.of(LocationRegularBusinessHours.builder().location(LOCATION_ID_1)
				.startHour(START_HOUR_1).startMinute(0).endHour(END_HOUR_1).endMinute(0).build()));

		calendar.setDayLightSavingInfoByLocation(Map.of(LOCATION_ID_1,
				List.of(DayLightSavingInfo.builder().start(dstStart).end(dstEnd).offsetInMinutes(dstOffset).build())));

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 02, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 06, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_DSTStartsAt_2h() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 1, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 2, 2, 0);
		int dstOffset = 60;

		calendar.setRegularBusinessHours(List.of(LocationRegularBusinessHours.builder().location(LOCATION_ID_1)
				.startHour(START_HOUR_1).startMinute(0).endHour(END_HOUR_1).endMinute(0).build()));

		calendar.setDayLightSavingInfoByLocation(Map.of(LOCATION_ID_1,
				List.of(DayLightSavingInfo.builder().start(dstStart).end(dstEnd).offsetInMinutes(dstOffset).build())));

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 01, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 06, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_DSTEndsAfter_2h() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 0, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 1, 8, 0);
		int dstOffset = 60;

		calendar.setRegularBusinessHours(List.of(LocationRegularBusinessHours.builder().location(LOCATION_ID_1)
				.startHour(START_HOUR_1).startMinute(0).endHour(END_HOUR_1).endMinute(0).build()));

		calendar.setDayLightSavingInfoByLocation(Map.of(LOCATION_ID_1,
				List.of(DayLightSavingInfo.builder().start(dstStart).end(dstEnd).offsetInMinutes(dstOffset).build())));

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 06, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_DSTEndsBefore_2h() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 0, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 1, 2, 0);
		int dstOffset = 60;

		calendar.setRegularBusinessHours(List.of(LocationRegularBusinessHours.builder().location(LOCATION_ID_1)
				.startHour(START_HOUR_1).startMinute(0).endHour(END_HOUR_1).endMinute(0).build()));

		calendar.setDayLightSavingInfoByLocation(Map.of(LOCATION_ID_1,
				List.of(DayLightSavingInfo.builder().start(dstStart).end(dstEnd).offsetInMinutes(dstOffset).build())));

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 05, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_DSTEndsAt_2h() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 0, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 1, 4, 0);
		int dstOffset = 60;

		calendar.setRegularBusinessHours(List.of(LocationRegularBusinessHours.builder().location(LOCATION_ID_1)
				.startHour(START_HOUR_1).startMinute(0).endHour(END_HOUR_1).endMinute(0).build()));

		calendar.setDayLightSavingInfoByLocation(Map.of(LOCATION_ID_1,
				List.of(DayLightSavingInfo.builder().start(dstStart).end(dstEnd).offsetInMinutes(dstOffset).build())));

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 05, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

}
