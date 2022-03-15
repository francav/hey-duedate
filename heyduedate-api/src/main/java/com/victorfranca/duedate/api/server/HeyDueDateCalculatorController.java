package com.victorfranca.duedate.api.server;

import java.time.LocalDateTime;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victorfranca.duedate.api.datasource.CalendarDataSource;
import com.victorfranca.duedate.api.datasource.CalendarDataSourceException;
import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calculator.log.CalculationLog;
import com.victorfranca.duedate.calendar.Calendar;

@RestController
@RequestMapping("/duedate")
class HeyDueDateCalculatorController {

	@Value("${calendar-datasource.type:}")
	private String dataSourceType;

	@Autowired
	private BeanFactory beanFactory;

	@GetMapping(value = "/{startDateTime}/{slaInMinutes}")
	public LocalDateTime getDueDate(
			@PathVariable("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
			@PathVariable("slaInMinutes") Integer slaInMinutes) {

		Calendar calendar = null;
		try {
			CalendarDataSource calendarDataSource = beanFactory.getBean(dataSourceType, CalendarDataSource.class);
			calendar = calendarDataSource.getCalendarData();
		} catch (CalendarDataSourceException e) {
			throw new RuntimeException("Internal Error", e);
		}

		return new DueDateCalculator().calculateDueDate(calendar, startDateTime, slaInMinutes);

	}

	@GetMapping(value = "/{startDateTime}/{slaInMinutes}/log")
	public CalculationLog getDueDateWithLog(
			@PathVariable("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
			@PathVariable("slaInMinutes") Integer slaInMinutes) {

		Calendar calendar = null;
		try {
			CalendarDataSource calendarDataSource = beanFactory.getBean(dataSourceType, CalendarDataSource.class);
			calendar = calendarDataSource.getCalendarData();
		} catch (CalendarDataSourceException e) {
			throw new RuntimeException("Internal Error", e);
		}

		return new DueDateCalculator().calculateDueDateWithLog(calendar, startDateTime, slaInMinutes);

	}

}
