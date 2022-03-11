package com.victorfranca.duedate.calendar.provider.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.provider.spi.CalendarDataSourceElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.InvalidCalendarDataSourceException;

//TODO given/when/then comments
public class JSONCalendarProviderTest {

	private static final String CALENDAR_ELEMENT = "regularBusinessHours";
	private static final String LOCATION_ELEMENT = "location";
	private static final String LOCATION_START_HOUR_ELEMENT = "startHour";
	private static final String LOCATION_START_MINUTE_ELEMENT = "startMinute";
	private static final String LOCATION_END_HOUR_ELEMENT = "endHour";
	private static final String LOCATION_END_MINUTE_ELEMENT = "endMinute";

	private static final String NON_BUSINESS_HOURS_DAYS_ELEMENT = "nonBusinessHoursDays";
	private static final String NON_BUSINESS_DAYS_VALUES_ELEMENT = "value";

	private static final String DST_INFO_LIST_ELEMENT = "daylightSavingTimeInfo";
	private static final String DST_START_ELEMENT = "start";
	private static final String DST_END_ELEMENT = "end";
	private static final String DST_OFFSET_ELEMENT = "offsetInMinutes";

	@Test
	@SuppressWarnings("unchecked")
	public void getCalendarTest_calendarShoulHaveDaylightSavingTime() {

		String location = "locationValue";
		int locationStartHour = 10;
		int locationStartMinute = 01;
		int locationEndHour = 12;
		int locationEndMinute = 02;

		String nonBusinessDate1 = "2022-03-17";
		String nonBusinessDate2 = "2022-12-31";
		
		String dstStart = "2022-03-13T02:00:00";
		String dstEnd = "2022-11-06T02:00:00";
		int dstOffset = 60;

		JSONObject calendarObject = new JSONObject();

		JSONArray locationsArray = new JSONArray();
		JSONObject locationObject = new JSONObject();
		locationObject.put(LOCATION_ELEMENT, location);
		locationObject.put(LOCATION_START_HOUR_ELEMENT, locationStartHour);
		locationObject.put(LOCATION_START_MINUTE_ELEMENT, locationStartMinute);
		locationObject.put(LOCATION_END_HOUR_ELEMENT, locationEndHour);
		locationObject.put(LOCATION_END_MINUTE_ELEMENT, locationEndMinute);
		locationsArray.add(locationObject);
		calendarObject.put(CALENDAR_ELEMENT, locationsArray);

		JSONArray nonBusinessDaysArray = new JSONArray();
		JSONObject nonBusinessDaysObject = new JSONObject();
		nonBusinessDaysObject.put(LOCATION_ELEMENT, location);
		nonBusinessDaysObject.put(NON_BUSINESS_DAYS_VALUES_ELEMENT, List.of(nonBusinessDate1, nonBusinessDate2));
		nonBusinessDaysArray.add(nonBusinessDaysObject);
		calendarObject.put(NON_BUSINESS_HOURS_DAYS_ELEMENT, nonBusinessDaysArray);

		JSONArray dstArray = new JSONArray();
		JSONObject dstObject = new JSONObject();
		dstObject.put(LOCATION_ELEMENT, location);
		dstObject.put(DST_START_ELEMENT, dstStart);
		dstObject.put(DST_END_ELEMENT, dstEnd);
		dstObject.put(DST_OFFSET_ELEMENT, dstOffset);
		dstArray.add(dstObject);
		calendarObject.put(DST_INFO_LIST_ELEMENT, dstArray);

		try {
			JSONCalendarProvider jsonCalendarProvider = new JSONCalendarProvider(calendarObject);
			Calendar calendar = jsonCalendarProvider.createCalendar();

			assertNotNull("DST info not found", calendar.getDayLightSavingInfoByLocation().get(location));
//			assertEquals(LocalDate.parse(nonBusinessDate1),
//					calendar.getNonBusinessDaysByLocation().get(location).get(0));
//			assertEquals(LocalDate.parse(nonBusinessDate2),
//					calendar.getNonBusinessDaysByLocation().get(location).get(1));

		} catch (InvalidCalendarDataSourceException e) {
			fail(e.getMessage());
		} catch (CalendarDataSourceElementNotFound e) {
			fail(e.getMessage());
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void getCalendarTest_calendarShoulHaveNonBusinessDays() {

		String location = "locationValue";
		int locationStartHour = 10;
		int locationStartMinute = 01;
		int locationEndHour = 12;
		int locationEndMinute = 02;

		String nonBusinessDate1 = "2022-03-17";
		String nonBusinessDate2 = "2022-12-31";

		JSONObject calendarObject = new JSONObject();

		JSONArray locationsArray = new JSONArray();
		JSONObject locationObject = new JSONObject();
		locationObject.put(LOCATION_ELEMENT, location);
		locationObject.put(LOCATION_START_HOUR_ELEMENT, locationStartHour);
		locationObject.put(LOCATION_START_MINUTE_ELEMENT, locationStartMinute);
		locationObject.put(LOCATION_END_HOUR_ELEMENT, locationEndHour);
		locationObject.put(LOCATION_END_MINUTE_ELEMENT, locationEndMinute);
		locationsArray.add(locationObject);
		calendarObject.put(CALENDAR_ELEMENT, locationsArray);

		JSONArray nonBusinessDaysArray = new JSONArray();
		JSONObject nonBusinessDaysObject = new JSONObject();
		nonBusinessDaysObject.put(LOCATION_ELEMENT, location);
		nonBusinessDaysObject.put(NON_BUSINESS_DAYS_VALUES_ELEMENT, List.of(nonBusinessDate1, nonBusinessDate2));
		nonBusinessDaysArray.add(nonBusinessDaysObject);
		calendarObject.put(NON_BUSINESS_HOURS_DAYS_ELEMENT, nonBusinessDaysArray);

		try {
			JSONCalendarProvider jsonCalendarProvider = new JSONCalendarProvider(calendarObject);
			Calendar calendar = jsonCalendarProvider.createCalendar();

			assertNotNull("Non-Business Days not found", calendar.getNonBusinessDaysByLocation().get(location));
			assertEquals(LocalDate.parse(nonBusinessDate1),
					calendar.getNonBusinessDaysByLocation().get(location).get(0));
			assertEquals(LocalDate.parse(nonBusinessDate2),
					calendar.getNonBusinessDaysByLocation().get(location).get(1));

		} catch (InvalidCalendarDataSourceException e) {
			fail(e.getMessage());
		} catch (CalendarDataSourceElementNotFound e) {
			fail(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getCalendarTest_calendarShoulHaveLocation() {

		String location = "locationValue";
		int locationStartHour = 10;
		int locationStartMinute = 01;
		int locationEndHour = 12;
		int locationEndMinute = 02;

		JSONArray locationsArray = new JSONArray();
		JSONObject locationObject = new JSONObject();
		locationObject.put(LOCATION_ELEMENT, location);
		locationObject.put(LOCATION_START_HOUR_ELEMENT, locationStartHour);
		locationObject.put(LOCATION_START_MINUTE_ELEMENT, locationStartMinute);
		locationObject.put(LOCATION_END_HOUR_ELEMENT, locationEndHour);
		locationObject.put(LOCATION_END_MINUTE_ELEMENT, locationEndMinute);
		locationsArray.add(locationObject);

		JSONObject calendarObject = new JSONObject();
		calendarObject.put(CALENDAR_ELEMENT, locationsArray);

		try {
			JSONCalendarProvider jsonCalendarProvider = new JSONCalendarProvider(calendarObject);
			Calendar calendar = jsonCalendarProvider.createCalendar();

			assertEquals(locationStartHour, calendar.getRegularBusinessHours().get(0).getStartHour());
			assertEquals(locationStartMinute, calendar.getRegularBusinessHours().get(0).getStartMinute());
			assertEquals(locationEndHour, calendar.getRegularBusinessHours().get(0).getEndHour());
			assertEquals(locationEndMinute, calendar.getRegularBusinessHours().get(0).getEndMinute());

		} catch (InvalidCalendarDataSourceException e) {
			fail(e.getMessage());
		} catch (CalendarDataSourceElementNotFound e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void getCalendarTest_whenNullDataSource_shouldThrowException() {

		JSONObject jsonObject = new JSONObject();

		assertThrows(InvalidCalendarDataSourceException.class, () -> {
			new JSONCalendarProvider(jsonObject);
		});

	}

	@Test
	public void getCalendarTest_whenEmptyCalendarRootNode_shouldThrowException() {

		JSONObject jsonObject = new JSONObject();

		assertThrows(InvalidCalendarDataSourceException.class, () -> {
			new JSONCalendarProvider(jsonObject);
		});

	}

	@Test
	@SuppressWarnings("unchecked")
	public void getCalendarTest_whenEmptyNotArrayRootNode_shouldThrowException() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CALENDAR_ELEMENT, new JSONObject());

		assertThrows(InvalidCalendarDataSourceException.class, () -> {
			new JSONCalendarProvider(jsonObject);
		});

	}

	@Test
	@SuppressWarnings("unchecked")
	public void getCalendarTest_whenNoLocationElement_shouldThrowException() {

		JSONArray locationsArray = new JSONArray();
		JSONObject locationObject = new JSONObject();
		locationsArray.add(locationObject);

		JSONObject calendarObject = new JSONObject();
		calendarObject.put(CALENDAR_ELEMENT, locationsArray);

		assertThrows(CalendarDataSourceElementNotFound.class, () -> {

			try {
				JSONCalendarProvider jsonCalendarProvider = new JSONCalendarProvider(calendarObject);
				jsonCalendarProvider.createCalendar();
			} catch (InvalidCalendarDataSourceException e) {
				fail();
			}

		});

	}
}
