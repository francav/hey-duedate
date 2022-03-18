package com.victorfranca.duedate.api.server;

import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victorfranca.duedate.api.datasource.CalendarDataSource;
import com.victorfranca.duedate.api.datasource.CalendarDataSourceException;

@RestController
class HeyDueDateCalendarsController {

	@Value("${calendar-datasource.type}")
	private String dataSourceType;

	@Autowired
	private BeanFactory beanFactory;

	@GetMapping(value = "/calendar")
	public List<String> getDueDateWithLog() {

		try {
			CalendarDataSource calendarDataSource = beanFactory.getBean(dataSourceType, CalendarDataSource.class);
			return calendarDataSource.getCalendars();
		} catch (CalendarDataSourceException e) {
			throw new RuntimeException("Internal Error", e);
		}

	}

}
