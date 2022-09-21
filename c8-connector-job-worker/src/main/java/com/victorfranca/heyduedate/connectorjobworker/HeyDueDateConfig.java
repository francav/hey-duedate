package com.victorfranca.heyduedate.connectorjobworker;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.victorfranca.duedate.calendar.datasource.CalendarDataSource;
import com.victorfranca.duedate.calendar.provider.json.JSONCalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.CalendarProvider;

@Configuration
public class HeyDueDateConfig {

	@Value("${calendar-datasource.type}")
	private String dataSourceType;

	@Autowired
	private BeanFactory beanFactory;

	@Bean(name = "calendarProvider")
	public CalendarProvider calendarProvider() {
		return new JSONCalendarProvider();
	}

	@Bean(name = "calendarDataSource")
	public CalendarDataSource calendarDataSource() {
		return beanFactory.getBean(dataSourceType, CalendarDataSource.class);
	}

}
