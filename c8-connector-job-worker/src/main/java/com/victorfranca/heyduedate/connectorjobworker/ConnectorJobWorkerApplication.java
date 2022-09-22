package com.victorfranca.heyduedate.connectorjobworker;

import static com.victorfranca.heyduedate.connectorjobworker.LocalDateToStringParser.parseToString;
import static com.victorfranca.heyduedate.connectorjobworker.StringToLocalDateParser.extractLocalDateTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.datasource.CalendarDataSource;
import com.victorfranca.duedate.calendar.datasource.CalendarDataSourceException;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

@SpringBootApplication
@EnableZeebeClient
public class ConnectorJobWorkerApplication {

	@Resource(name = "calendarDataSource")
	private CalendarDataSource calendarDataSource;

	public static void main(String[] args) {
		SpringApplication.run(ConnectorJobWorkerApplication.class, args);
	}

	@ZeebeWorker(type = "io.camunda:hey-due-date:1", autoComplete = true)
	public Map<String, Object> calculateDueDate(final JobClient client, final ActivatedJob job,
			@ZeebeVariable String calendar, @ZeebeVariable String startDate, @ZeebeVariable String sla) {

		LocalDateTime localDateTime = extractLocalDateTime(startDate);

		Calendar heyDueDateCalendar = null;
		try {
			heyDueDateCalendar = calendarDataSource.getCalendarData(calendar);
		} catch (CalendarDataSourceException e) {
			throw new RuntimeException("Internal Error", e);
		}

		HashMap<String, Object> processVariables = new HashMap<>();
		LocalDateTime dueDate = calculateDueDate(sla, localDateTime, heyDueDateCalendar);

		processVariables.put("dueDateTime", parseToString(dueDate));
		processVariables.put("dueDateDuration", calculateDuration(dueDate));

		return processVariables;
	}

	private String calculateDuration(LocalDateTime dueDate) {
		return String.valueOf(Duration.between(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
				dueDate.truncatedTo(ChronoUnit.SECONDS)));
	}

	private LocalDateTime calculateDueDate(String sla, LocalDateTime localDateTime, Calendar heyDueDateCalendar) {
		return new DueDateCalculator().calculateDueDate(heyDueDateCalendar, localDateTime, Long.valueOf(sla) * 60)
				.getDueDateTime();
	}

}
