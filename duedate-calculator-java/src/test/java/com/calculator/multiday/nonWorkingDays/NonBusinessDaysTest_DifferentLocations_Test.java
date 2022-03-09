package com.calculator.multiday.nonWorkingDays;

import static com.calculator.CalendarBlockDataBuilder.createCalendarBlock;
import static com.calculator.NonBusinessDayProviderFactoryBuilder.createNonBusinessDayProviderFactory;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import com.calculator.CalendarProvider;
import com.calculator.DueDateCalculator;
import com.calendar.Calendar;
import com.calendar.CalendarDay;;

public class NonBusinessDaysTest_DifferentLocations_Test {

	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final String LOCATION_ID_2 = "LOCATION_ID_2";

	@Test
	public void calculateDueDateTest_nonBusinessFirstDayFirstBlock_2blocks_05_00_2h() {
		//Given
		String nbdLocation1 = LOCATION_ID_1;
		LocalDate nbdDate = LocalDate.of(2022, 1, 1);

		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		String block2Location = LOCATION_ID_2;
		LocalDateTime block2Start = LocalDateTime.of(2022, 1, 1, 12, 0);
		LocalDateTime block2End = LocalDateTime.of(2022, 1, 1, 18, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {
			@Override
			public Calendar getCalendar() {
				CalendarDay calendarDay = new CalendarDay();

				calendarDay.setNonBusinessDaysProviderFactory(
						createNonBusinessDayProviderFactory(nbdLocation1, nbdDate, null, null));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End))
						.addCalendarBlock(createCalendarBlock(block2Location, block2Start, block2End));

				return new Calendar(calendarDay);
			}
		});

		//WHen
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 05, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 14, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_nonBusinessFirstDayFirstBlock_2blocks_05_00_420h() {
		//Given
		String nbdLocation = LOCATION_ID_1;
		LocalDate nbdDate = LocalDate.of(2022, 1, 1);

		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		String block2Location = LOCATION_ID_2;
		LocalDateTime block2Start = LocalDateTime.of(2022, 1, 1, 12, 0);
		LocalDateTime block2End = LocalDateTime.of(2022, 1, 1, 18, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {
			@Override
			public Calendar getCalendar() {

				CalendarDay calendarDay = new CalendarDay();

				calendarDay.setNonBusinessDaysProviderFactory(

						createNonBusinessDayProviderFactory(nbdLocation, nbdDate, null, null));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End))
						.addCalendarBlock(createCalendarBlock(block2Location, block2Start, block2End));

				return new Calendar(calendarDay);
			}
		});
		
		//When
		int slaInMinutes = 60 * 7;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 05, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 4, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_nonBusinessSecondtDayFirstBlock_2blocks_17_00_2h() {
		//Given
		String nbdLocation1 = LOCATION_ID_1;
		LocalDate nbdDate = LocalDate.of(2022, 1, 2);

		String block1Location = LOCATION_ID_1;
		LocalDateTime block1Start = LocalDateTime.of(2022, 1, 1, 3, 0);
		LocalDateTime block1End = LocalDateTime.of(2022, 1, 1, 6, 0);

		String block2Location = LOCATION_ID_2;
		LocalDateTime block2Start = LocalDateTime.of(2022, 1, 1, 12, 0);
		LocalDateTime block2End = LocalDateTime.of(2022, 1, 1, 18, 0);

		dueDateCalculator = new DueDateCalculator(new CalendarProvider() {
			@Override
			public Calendar getCalendar() {
				CalendarDay calendarDay = new CalendarDay();

				calendarDay.setNonBusinessDaysProviderFactory(
						createNonBusinessDayProviderFactory(nbdLocation1, nbdDate, null, null));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End))
						.addCalendarBlock(createCalendarBlock(block2Location, block2Start, block2End));

				return new Calendar(calendarDay);
			}
		});

		//When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 17, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 13, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

}
