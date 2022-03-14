package com.victorfranca.duedate.api.datasource.file;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.victorfranca.duedate.api.datasource.CalendarDataSource;
import com.victorfranca.duedate.api.datasource.CalendarDataSourceException;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.provider.json.JSONCalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.exception.CalendarElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.exception.InvalidCalendarException;

@Component("fileCalendarDS")
public class FileCalendarDataSource implements CalendarDataSource {

	@Value("${calendar-datasource-file.name:}")
	private String fileName;

	public Calendar getCalendarData() throws CalendarDataSourceException {
		JSONParser jsonParser = new JSONParser();

		FileReader reader;
		try {
			reader = new FileReader(getFileFromResource(fileName));

			// TODO replace new JSONCalendarProvider ?
			return new JSONCalendarProvider((JSONObject) jsonParser.parse(reader)).createCalendar();
		} catch (IOException | URISyntaxException | ParseException | CalendarElementNotFound
				| InvalidCalendarException e) {
			throw new CalendarDataSourceException(e.getMessage(), e);
		}

	}

	private File getFileFromResource(String fileName) throws URISyntaxException {

		ClassLoader classLoader = FileCalendarDataSource.class.getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("File not found! " + fileName);
		} else {
			try {
				return new File(resource.toURI());
			} catch (URISyntaxException e) {
				throw e;
			}
		}

	}

}
