package com.victorfranca.duedate.rest.calculator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.provider.json.JSONCalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.CalendarDataSourceElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.CalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.InvalidCalendarDataSourceException;

@RestController
@RequestMapping("/duedate")
class HeyDueDateCalculatorController {

	@GetMapping(value = "/{startDateTime}/{slaInMinutes}")
	public LocalDateTime getDueDate(
			@PathVariable("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
			@PathVariable("slaInMinutes") Integer slaInMinutes) {

		CalendarProvider jsonCalendarProvider = null;
		Calendar calendar = null;
		try {
			jsonCalendarProvider = new JSONCalendarProvider(getCalendarData("calendar.json"));
			calendar = jsonCalendarProvider.createCalendar();
		} catch (InvalidCalendarDataSourceException | CalendarDataSourceElementNotFound | URISyntaxException
				| IOException | ParseException e) {
			throw new RuntimeException("Internal Error");
		}

		return new DueDateCalculator().calculateDueDate(calendar, startDateTime, slaInMinutes);

	}

	public JSONObject getCalendarData(String fileName) throws URISyntaxException, IOException, ParseException {
		JSONParser jsonParser = new JSONParser();

		FileReader reader = new FileReader(getFileFromResource(fileName));
		return (JSONObject) jsonParser.parse(reader);
	}

	private File getFileFromResource(String fileName) throws URISyntaxException {

		ClassLoader classLoader = HeyDueDateCalculatorController.class.getClassLoader();
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
