package com.victorfranca.duedate.api.server;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.victorfranca.duedate.api.datasource.CalendarDataSource;
import com.victorfranca.duedate.api.datasource.CalendarDataSourceException;
import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calculator.log.CalculationLog;
import com.victorfranca.duedate.calendar.Calendar;

@RestController
class HeyDueDateCalculatorController {

	@Value("${calendar-datasource.type}")
	private String dataSourceType;

	@Autowired
	private BeanFactory beanFactory;

	@GetMapping(value = "/duedate")
	public CalculationLog getDueDateWithLog(
			@RequestParam("calendar") String calendarName,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
			@RequestParam("sla") Integer slaInMinutes, @RequestParam("log") Boolean logEnabled) {

		startDateTime = startDateTime.truncatedTo(ChronoUnit.MINUTES);

		Calendar calendar = null;
		try {
			CalendarDataSource calendarDataSource = beanFactory.getBean(dataSourceType, CalendarDataSource.class);
			calendar = calendarDataSource.getCalendarData(calendarName);
		} catch (CalendarDataSourceException e) {
			throw new RuntimeException("Internal Error", e);
		}

		if (logEnabled) {
			return new DueDateCalculator().calculateDueDateWithLog(calendar, startDateTime, slaInMinutes);
		}else {
			return new DueDateCalculator().calculateDueDate(calendar, startDateTime, slaInMinutes);
		}

	}

}
