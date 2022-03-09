package com.victorfranca.duedate.rest.calculator;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

@RestController
@RequestMapping("/duedate")
class DueDateCalculatorController {

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";

	@GetMapping(value = "/{startDateTime}/{slaInMinutes}")
	public LocalDateTime findById(
			@PathVariable("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
			@PathVariable("slaInMinutes") Integer slaInMinutes) {

		Calendar calendar = new Calendar();

		calendar.setLocationRegularBusinessHoursList(List.of(LocationRegularBusinessHours.builder()
				.locationID(LOCATION_ID_1).startHour(3).startMinute(0).endHour(6).endMinute(0).build()));

		DueDateCalculator dueDateCalculator = new DueDateCalculator();
		return dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes);

	}

}
