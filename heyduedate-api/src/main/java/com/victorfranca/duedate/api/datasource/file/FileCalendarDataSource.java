package com.victorfranca.duedate.api.datasource.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.victorfranca.duedate.api.datasource.CalendarDataSource;
import com.victorfranca.duedate.api.datasource.CalendarDataSourceException;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.provider.json.JSONCalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.exception.CalendarElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.exception.InvalidCalendarException;

@Component("fileCalendarDS")
public class FileCalendarDataSource implements CalendarDataSource {

	@Value("classpath:${calendar-datasource-file.name}")
	private Resource resourceFile;

	public Calendar getCalendarData() throws CalendarDataSourceException {
		JSONParser jsonParser = new JSONParser();

		try {
			InputStream resource = resourceFile.getInputStream();

			// TODO replace new JSONCalendarProvider ?
			return new JSONCalendarProvider((JSONObject) jsonParser.parse(new InputStreamReader(resource)))
					.createCalendar();
		} catch (IOException | ParseException | CalendarElementNotFound | InvalidCalendarException e) {
			throw new CalendarDataSourceException(e.getMessage(), e);
		}

	}

}
