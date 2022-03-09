package com.victorfranca.duedate.calculator.singleday.multiblock;

import static com.victorfranca.duedate.calculator.CalendarBlockDataBuilder.createCalendarBlock;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.victorfranca.duedate.calculator.CalendarProvider;
import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.CalendarDay;

public class MultiBlock_StartDateBeforeFirstCalendarBlock_Test {

	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final String LOCATION_ID_2 = "LOCATION_ID_2";

	@Before
	public void inid() {
		//Given
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

				calendarDay.addCalendarBlock(createCalendarBlock(block1Location, block1Start, block1End))
						.addCalendarBlock(createCalendarBlock(block2Location, block2Start, block2End));
				return new Calendar(calendarDay);
			}
		});
	}

	@Test
	public void calculateDueDateTest_01_00_4h() {
		//When
		int slaInMinutes = 60 * 4;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 1, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 13, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_01_01_4h() {
		//When
		int slaInMinutes = 60 * 4;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 1, 01);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 13, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_01_59_4h() {
		//When
		int slaInMinutes = 60 * 4;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 1, 59);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 13, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

}
