package com.victorfranca.heyduedate.camunda;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.datasource.CalendarDataSource;
import com.victorfranca.duedate.calendar.datasource.CalendarDataSourceException;

@Component
public class HeyDueDateCalculator implements JavaDelegate {

	@Autowired
	private CalendarDataSource calendarDataSource;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String calendarName = "calendar-sample";
		LocalDateTime startDateTimeUTC = ((Date) execution.getVariableLocal("startDateTime")).toInstant()
				.atZone(ZoneId.of("UTC")).toLocalDateTime();
		
		Long slaInMinutes = ((Long) execution.getVariableLocal("slaInMinutes"));

		Calendar calendar = null;
		try {
			calendar = calendarDataSource.getCalendarData(calendarName);
		} catch (CalendarDataSourceException e) {
			throw new RuntimeException("Internal Error", e);
		}

		LocalDateTime dueDateUTC = new DueDateCalculator().calculateDueDate(calendar, startDateTimeUTC, slaInMinutes)
				.getDueDateTime();

		execution.setVariable("dueDate", Date.from(dueDateUTC.atZone(ZoneId.of("UTC")).toInstant()));

		execution.setVariable("utcStartDateTimeUTC", startDateTimeUTC.toString());
		execution.setVariable("dueDateUTC", dueDateUTC.toString());
	}

}
