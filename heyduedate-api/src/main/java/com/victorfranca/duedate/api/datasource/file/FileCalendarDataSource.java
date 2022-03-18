package com.victorfranca.duedate.api.datasource.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.victorfranca.duedate.api.datasource.CalendarDataSource;
import com.victorfranca.duedate.api.datasource.CalendarDataSourceException;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.provider.json.JSONCalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.exception.CalendarElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.exception.InvalidCalendarException;

@Component("fileCalendarDS")
public class FileCalendarDataSource implements CalendarDataSource {

	private static final String JSON_EXTENSION = ".json";

	@Value("${calendar-datasource-folder.name}")
	private String resourceFolderName;

	@Autowired
	private ResourceLoader resourceLoader;

	public Calendar getCalendarData(String calendarFileName) throws CalendarDataSourceException {
		JSONParser jsonParser = new JSONParser();

		try {

			InputStream resourceInputStream = resourceLoader
					.getResource("classpath:" + resourceFolderName + calendarFileName + JSON_EXTENSION)
					.getInputStream();

			// TODO replace new JSONCalendarProvider ?
			return new JSONCalendarProvider((JSONObject) jsonParser.parse(new InputStreamReader(resourceInputStream)))
					.createCalendar();
		} catch (IOException | ParseException | CalendarElementNotFound | InvalidCalendarException e) {
			throw new CalendarDataSourceException(e.getMessage(), e);
		}

	}

	@Override
	public List<String> getCalendars() throws CalendarDataSourceException {
		try {
			File[] calendarsFiles = resourceLoader.getResource("classpath:" + resourceFolderName).getFile().listFiles();
			return Arrays.asList(calendarsFiles).stream().map(o -> o.getName().replaceAll(JSON_EXTENSION, ""))
					.collect(Collectors.toList());

		} catch (IOException e) {
			throw new CalendarDataSourceException(e.getMessage(), e);
		}
	}

}
