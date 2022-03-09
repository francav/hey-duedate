package com.slacalculatorrest.calculator;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calculator.CalendarProvider;
import com.calculator.DueDateCalculator;
import com.calendar.Calendar;
import com.calendar.CalendarBlock;
import com.calendar.CalendarDay;

@RestController
@RequestMapping("/duedate")
class DueDateCalculatorController {

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";

	@GetMapping(value = "/{startDateTime}/{slaInMinutes}")
	public LocalDateTime findById(
			@PathVariable("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
			@PathVariable("slaInMinutes") Integer slaInMinutes) {

		DueDateCalculator dueDateCalculator = new DueDateCalculator(new CalendarProvider() {
			@Override
			public Calendar getCalendar() {

				CalendarDay calendarDay = new CalendarDay();
				CalendarBlock calendarBlock1 = new CalendarBlock(LOCATION_ID_1,
						startDateTime.withHour(3).withMinute(0).withSecond(0).withNano(0),
						startDateTime.withHour(6).withMinute(0).withSecond(0).withNano(0));

				calendarDay.addCalendarBlock(calendarBlock1);

				return new Calendar(calendarDay);
			}
		});

		LocalDateTime dueDateTime = dueDateCalculator.calculateDueDate(startDateTime, slaInMinutes);

		return dueDateTime;

	}

}
