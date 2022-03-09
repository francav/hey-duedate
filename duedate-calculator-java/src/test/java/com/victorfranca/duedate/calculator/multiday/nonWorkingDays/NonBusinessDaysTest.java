package com.victorfranca.duedate.calculator.multiday.nonWorkingDays;

import static com.victorfranca.duedate.calculator.CalendarBlockDataBuilder.createCalendarBlock;
import static com.victorfranca.duedate.calculator.NonBusinessDayProviderFactoryBuilder.createNonBusinessDayProviderFactory;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import com.victorfranca.duedate.calculator.CalendarProvider;
import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.CalendarDay;

public class NonBusinessDaysTest {

	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final String LOCATION_ID_2 = "LOCATION_ID_2";

	@Test
	public void calculateDueDateTest_nonBusinessFirstDay_2blocks_17_00_2h() {
		//Given
		String nbdLocation1 = LOCATION_ID_1;
		LocalDate nbdDate1 = LocalDate.of(2022, 1, 1);

		String nbdLocation2 = LOCATION_ID_2;
		LocalDate nbdDate2 = LocalDate.of(2022, 1, 1);

		String block1Location2 = LOCATION_ID_1;
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
						createNonBusinessDayProviderFactory(nbdLocation1, nbdDate1, nbdLocation2, nbdDate2));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location2, block1Start, block1End))
						.addCalendarBlock(createCalendarBlock(block2Location, block2Start, block2End));

				return new Calendar(calendarDay);
			}
		});

		//When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 17, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 05, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_nonBusinessSecondDay_2blocks_17_00_2h() {
		//Given
		String nbdLocation1 = LOCATION_ID_1;
		LocalDate nbdDate1 = LocalDate.of(2022, 1, 2);

		String nbdLocation2 = LOCATION_ID_2;
		LocalDate nbdDate2 = LocalDate.of(2022, 1, 2);

		String block1Location2 = LOCATION_ID_1;
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
						createNonBusinessDayProviderFactory(nbdLocation1, nbdDate1, nbdLocation2, nbdDate2));

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location2, block1Start, block1End))
						.addCalendarBlock(createCalendarBlock(block2Location, block2Start, block2End));

				return new Calendar(calendarDay);
			}
		});

		//When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 17, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 3, 04, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

}
