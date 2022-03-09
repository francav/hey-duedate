package com.victorfranca.duedate.calculator.multiday.daylightsaving;

import static com.victorfranca.duedate.calculator.CalendarBlockDataBuilder.createCalendarBlock;
import static com.victorfranca.duedate.calculator.DayLightSavingProviderBuilder.createDayLightSavingProvider;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import com.victorfranca.duedate.calculator.CalendarProvider;
import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.CalendarDay;

//TODO Mix DST and no-DST in different locations
public class DayLightSavingTest {

	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";

	@Test
	public void calculateDueDateTest_DSTStartsAfter_2h() {
		// Given
		String dstLocation = LOCATION_ID_1;
		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 1, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 2, 2, 0);
		int dstOffset = 60;

		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {

			@Override
			public Calendar getCalendar() {
				CalendarDay calendarDay = new CalendarDay();

				calendarDay.setDayLightSavingProvider(
						createDayLightSavingProvider(dstLocation, dstStart, dstEnd, dstOffset));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End));

				return new Calendar(calendarDay);
			}
		});

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 00, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 06, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_DSTBefore_2h() {
		// Given
		String dstLocation = LOCATION_ID_1;
		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 1, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 2, 2, 0);
		int dstOffset = 60;

		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {

			@Override
			public Calendar getCalendar() {
				CalendarDay calendarDay = new CalendarDay();

				calendarDay.setDayLightSavingProvider(
						createDayLightSavingProvider(dstLocation, dstStart, dstEnd, dstOffset));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End));

				return new Calendar(calendarDay);

			}
		});

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 02, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 06, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_DSTStartsAt_2h() {

		// Given
		String dstLocation = LOCATION_ID_1;
		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 1, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 2, 2, 0);
		int dstOffset = 60;

		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {

			@Override
			public Calendar getCalendar() {
				CalendarDay calendarDay = new CalendarDay();

				calendarDay.setDayLightSavingProvider(
						createDayLightSavingProvider(dstLocation, dstStart, dstEnd, dstOffset));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End));

				return new Calendar(calendarDay);
			}
		});

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 01, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 06, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_DSTEndsAfter_2h() {
		// Given
		String dstLocation = LOCATION_ID_1;
		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 0, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 1, 8, 0);
		int dstOffset = 60;

		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {

			@Override
			public Calendar getCalendar() {
				CalendarDay calendarDay = new CalendarDay();

				calendarDay.setDayLightSavingProvider(
						createDayLightSavingProvider(dstLocation, dstStart, dstEnd, dstOffset));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End));

				return new Calendar(calendarDay);
			}
		});

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 06, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_DSTEndsBefore_2h() {
		// Given
		String dstLocation1 = LOCATION_ID_1;
		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 0, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 1, 2, 0);
		int dstOffset = 60;

		String block1Location1 = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {

			@Override
			public Calendar getCalendar() {
				CalendarDay calendarDay = new CalendarDay();

				calendarDay.setDayLightSavingProvider(
						createDayLightSavingProvider(dstLocation1, dstStart, dstEnd, dstOffset));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location1, block1Start, block1End));

				return new Calendar(calendarDay);
			}
		});

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 05, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_DSTEndsAt_2h() {
		// Given
		String dstLocation = LOCATION_ID_1;
		LocalDateTime dstStart = LocalDateTime.of(2022, 1, 1, 0, 0);
		LocalDateTime dstEnd = LocalDateTime.of(2022, 1, 1, 4, 0);
		int dstOffset = 60;

		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {

			@Override
			public Calendar getCalendar() {
				CalendarDay calendarDay = new CalendarDay();

				calendarDay.setDayLightSavingProvider(
						createDayLightSavingProvider(dstLocation, dstStart, dstEnd, dstOffset));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End));

				return new Calendar(calendarDay);
			}
		});

		// When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 05, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

}
