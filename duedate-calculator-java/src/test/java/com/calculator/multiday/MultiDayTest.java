package com.calculator.multiday;

import static com.calculator.CalendarBlockDataBuilder.createCalendarBlock;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.calculator.CalendarProvider;
import com.calculator.DueDateCalculator;
import com.calendar.Calendar;
import com.calendar.CalendarDay;

public class MultiDayTest {

	private DueDateCalculator dueDateCalculator;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";
	private static final String LOCATION_ID_2 = "LOCATION_ID_2";

	@Before
	public void init() {

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
				
				
				calendarDay
						.addCalendarBlock(createCalendarBlock(block1Location, block1Start,
								block1End))
						.addCalendarBlock(createCalendarBlock(block2Location, block2Start,
								block2End));

				return new Calendar(calendarDay);
			}
		});

	}

	@Test
	public void calculateDueDateTest_2blocks_17_00_2h() {
		
		//When
		int slaInMinutes = 60 * 2;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 17, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 04, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_2blocks_11_00_7h() {
		//When
		int slaInMinutes = 60 * 7;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 11, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 2, 04, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

	@Test
	public void calculateDueDateTest_2blocks_11_00_24h() {
		//When
		int slaInMinutes = 60 * 24;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 11, 00);

		//Then
		assertEquals(LocalDateTime.of(2022, 1, 3, 18, 00),
				dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes));
	}

}
