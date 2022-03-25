/*
 * Copyright WKS Power Limited under one or more contributor license agreements. 
 * See the LICENSE file distributed with this work for additional information regarding 
 * copyright ownership. WKS Power licenses this file to you under the 
 * GNU AFFERO GENERAL PUBLIC LICENSE v3.0; you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/agpl-3.0.en.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.victorfranca.duedate.calendar.provider.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.provider.spi.exception.CalendarElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.exception.InvalidCalendarException;

//TODO given/when/then comments
/**
 * @author victor.franca
 *
 */
public class JSONCalendarProviderTest {

	private static final String CALENDAR_ELEMENT = "regularBusinessHours";
	private static final String LOCATION_ELEMENT = "location";
	private static final String LOCATION_START_HOUR_ELEMENT = "startHour";
	private static final String LOCATION_START_MINUTE_ELEMENT = "startMinute";
	private static final String LOCATION_END_HOUR_ELEMENT = "endHour";
	private static final String LOCATION_END_MINUTE_ELEMENT = "endMinute";

	private static final String NON_BUSINESS_HOURS_DAYS_ELEMENT = "nonBusinessHoursDays";
	private static final String NON_BUSINESS_DAYS_VALUES_ELEMENT = "value";

	private static final String PARTIAL_BUSINESS_HOURS_DAYS_ELEMENT = "partialBusinessHours";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_DATE_ELEMENT = "date";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_START_HOUR_ELEMENT = "startHour";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_START_MINUTE_ELEMENT = "startMinute";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_END_HOUR_ELEMENT = "endHour";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_END_MINUTE_ELEMENT = "endMinute";

	private static final String DST_INFO_LIST_ELEMENT = "daylightSavingTimeInfo";
	private static final String DST_START_ELEMENT = "start";
	private static final String DST_END_ELEMENT = "end";
	private static final String DST_OFFSET_ELEMENT = "offsetInMinutes";

	@Test
	public void getCalendarTest_calendarShoulHavePartialBusinessHours() {

		String location = "locationValue";
		int locationStartHour = 10;
		int locationStartMinute = 00;
		int locationEndHour = 12;
		int locationEndMinute = 00;

		LocalDate nonPartialBusinessHoursDate = LocalDate.of(2022, 03, 15);

		LocalDate partialBusinessHoursDate = LocalDate.of(2022, 03, 16);
		int partialStartHour = 8;
		int partialStartMinute = 00;
		int partialEndHour = 10;
		int partialEndMinute = 00;

		JSONObject calendarObject = new JSONObject();

		JSONArray locationsArray = new JSONArray();
		JSONObject locationObject = new JSONObject();
		locationObject.put(LOCATION_ELEMENT, location);
		locationObject.put(LOCATION_START_HOUR_ELEMENT, locationStartHour);
		locationObject.put(LOCATION_START_MINUTE_ELEMENT, locationStartMinute);
		locationObject.put(LOCATION_END_HOUR_ELEMENT, locationEndHour);
		locationObject.put(LOCATION_END_MINUTE_ELEMENT, locationEndMinute);

		JSONArray locationPartialBusinessHoursArray = new JSONArray();
		JSONObject locationPartialBusinessHour1 = new JSONObject();
		locationPartialBusinessHour1.put(PARTIAL_BUSINESS_HOURS_DAYS_DATE_ELEMENT, partialBusinessHoursDate);
		locationPartialBusinessHour1.put(PARTIAL_BUSINESS_HOURS_DAYS_START_HOUR_ELEMENT, partialStartHour);
		locationPartialBusinessHour1.put(PARTIAL_BUSINESS_HOURS_DAYS_START_MINUTE_ELEMENT, partialStartMinute);
		locationPartialBusinessHour1.put(PARTIAL_BUSINESS_HOURS_DAYS_END_HOUR_ELEMENT, partialEndHour);
		locationPartialBusinessHour1.put(PARTIAL_BUSINESS_HOURS_DAYS_END_MINUTE_ELEMENT, partialEndMinute);
		locationPartialBusinessHoursArray.add(locationPartialBusinessHour1);

		locationObject.put(PARTIAL_BUSINESS_HOURS_DAYS_ELEMENT, locationPartialBusinessHoursArray);
		locationsArray.add(locationObject);
		calendarObject.put(CALENDAR_ELEMENT, locationsArray);

		try {
			JSONCalendarProvider jsonCalendarProvider = new JSONCalendarProvider(calendarObject);
			Calendar calendar = jsonCalendarProvider.createCalendar();

			assertNotNull("Partial Business Hours info not found",
					calendar.getRegularBusinessHours().get(0).getPartialBusinessHoursMap());
			assertFalse(calendar.getRegularBusinessHours().get(0).getPartialBusinessHoursMap().isEmpty());

			assertEquals(locationStartHour,
					calendar.getRegularBusinessHours().get(0).getStartHour(nonPartialBusinessHoursDate));
			assertEquals(locationStartMinute,
					calendar.getRegularBusinessHours().get(0).getStartMinute(nonPartialBusinessHoursDate));
			assertEquals(locationEndHour,
					calendar.getRegularBusinessHours().get(0).getEndHour(nonPartialBusinessHoursDate));
			assertEquals(locationEndMinute,
					calendar.getRegularBusinessHours().get(0).getEndMinute(nonPartialBusinessHoursDate));

			assertEquals(partialStartHour,
					calendar.getRegularBusinessHours().get(0).getStartHour(partialBusinessHoursDate));
			assertEquals(partialStartMinute,
					calendar.getRegularBusinessHours().get(0).getStartMinute(partialBusinessHoursDate));
			assertEquals(partialEndHour,
					calendar.getRegularBusinessHours().get(0).getEndHour(partialBusinessHoursDate));
			assertEquals(partialEndMinute,
					calendar.getRegularBusinessHours().get(0).getEndMinute(partialBusinessHoursDate));

		} catch (InvalidCalendarException e) {
			fail(e.getMessage());
		} catch (CalendarElementNotFound e) {
			fail(e.getMessage());
		}
	}

	@Test
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
			assertEquals(LocalDate.parse(nonBusinessDate1),
					calendar.getNonBusinessDaysByLocation().get(location).get(0));
			assertEquals(LocalDate.parse(nonBusinessDate2),
					calendar.getNonBusinessDaysByLocation().get(location).get(1));

		} catch (InvalidCalendarException e) {
			fail(e.getMessage());
		} catch (CalendarElementNotFound e) {
			fail(e.getMessage());
		}
	}

	@Test
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

		} catch (InvalidCalendarException e) {
			fail(e.getMessage());
		} catch (CalendarElementNotFound e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void getCalendarTest_calendarShoulHaveLocation() {

		String location = "locationValue";
		LocalDate date = LocalDate.of(2021, 1, 1);
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

			assertEquals(locationStartHour, calendar.getRegularBusinessHours().get(0).getStartHour(date));
			assertEquals(locationStartMinute, calendar.getRegularBusinessHours().get(0).getStartMinute(date));
			assertEquals(locationEndHour, calendar.getRegularBusinessHours().get(0).getEndHour(date));
			assertEquals(locationEndMinute, calendar.getRegularBusinessHours().get(0).getEndMinute(date));

		} catch (InvalidCalendarException e) {
			fail(e.getMessage());
		} catch (CalendarElementNotFound e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void getCalendarTest_whenNullDataSource_shouldThrowException() {

		JSONObject jsonObject = new JSONObject();

		assertThrows(InvalidCalendarException.class, () -> {
			new JSONCalendarProvider(jsonObject);
		});

	}

	@Test
	public void getCalendarTest_whenEmptyCalendarRootNode_shouldThrowException() {

		JSONObject jsonObject = new JSONObject();

		assertThrows(InvalidCalendarException.class, () -> {
			new JSONCalendarProvider(jsonObject);
		});

	}

	@Test
	public void getCalendarTest_whenEmptyNotArrayRootNode_shouldThrowException() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CALENDAR_ELEMENT, new JSONObject());

		assertThrows(InvalidCalendarException.class, () -> {
			new JSONCalendarProvider(jsonObject);
		});

	}

	@Test
	public void getCalendarTest_whenNoLocationElement_shouldThrowException() {

		JSONArray locationsArray = new JSONArray();
		JSONObject locationObject = new JSONObject();
		locationsArray.add(locationObject);

		JSONObject calendarObject = new JSONObject();
		calendarObject.put(CALENDAR_ELEMENT, locationsArray);

		assertThrows(CalendarElementNotFound.class, () -> {

			try {
				JSONCalendarProvider jsonCalendarProvider = new JSONCalendarProvider(calendarObject);
				jsonCalendarProvider.createCalendar();
			} catch (InvalidCalendarException e) {
				fail();
			}

		});

	}
}
