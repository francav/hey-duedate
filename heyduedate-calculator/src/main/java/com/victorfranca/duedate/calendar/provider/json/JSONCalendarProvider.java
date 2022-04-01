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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.victorfranca.duedate.calculator.daylightsaving.DayLightSavingInfo;
import com.victorfranca.duedate.calculator.partialbusinesshours.PartialBusinessHour;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;
import com.victorfranca.duedate.calendar.provider.spi.CalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.exception.CalendarElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.exception.InvalidCalendarException;

/**
 * @author victor.franca
 *
 */
public class JSONCalendarProvider implements CalendarProvider {

	private List<JSONObject> regularBusinessHoursDays;
	private List<JSONObject> nonBusinessHoursDays;
	private List<JSONObject> dstInfoList;

	private static final String REGULAR_BUSINESS_HOURS = "regularBusinessHours";
	private static final String LOCATION_NODE = "location";
	private static final String LOCATION_START_HOUR = "startHour";
	private static final String LOCATION_START_MINUTE = "startMinute";
	private static final String LOCATION_END_HOUR = "endHour";
	private static final String LOCATION_END_MINUTE = "endMinute";

	private static final String PARTIAL_BUSINESS_HOURS_DAYS_ELEMENT = "partialBusinessHours";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_DATE_ELEMENT = "date";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_START_HOUR_ELEMENT = "startHour";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_START_MINUTE_ELEMENT = "startMinute";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_END_HOUR_ELEMENT = "endHour";
	private static final String PARTIAL_BUSINESS_HOURS_DAYS_END_MINUTE_ELEMENT = "endMinute";

	private static final String NON_BUSINESS_HOURS_DAYS = "nonBusinessHoursDays";
	private static final String NON_BUSINESS_DAYS = "value";

	private static final String DST_INFO_LIST = "daylightSavingTimeInfo";
	private static final String DST_START = "start";
	private static final String DST_END = "end";
	private static final String DST_OFFSET = "offsetInMinutes";

	@Override
	public Calendar createCalendar(JSONObject calendarDataSource) throws CalendarElementNotFound, InvalidCalendarException {
		try {
			validateCalendarDataSource(calendarDataSource);
		} catch (InvalidCalendarException e) {
			throw e;
		}

		this.regularBusinessHoursDays = (List) calendarDataSource.get(REGULAR_BUSINESS_HOURS);
		this.nonBusinessHoursDays = (List) calendarDataSource.get(NON_BUSINESS_HOURS_DAYS);
		this.dstInfoList = (List) calendarDataSource.get(DST_INFO_LIST);
		
		Calendar calendar = new Calendar();

		addLocationRegularBusinessHours(calendar);

		if (nonBusinessHoursDays != null) {
			addNonBusinessHoursDays(calendar);
		}

		if (dstInfoList != null) {
			addDSTInfo(calendar);
		}

		return calendar;
	}

	private void addLocationRegularBusinessHours(Calendar calendar) throws CalendarElementNotFound {
		for (Object calendarElement : regularBusinessHoursDays) {

			Map<String, Object> calendarElementJSON = ((Map<String, Object>) calendarElement);

			calendar.addLocationRegularBusinessHours(LocationRegularBusinessHours.builder()

					.location(getString(LOCATION_NODE, calendarElementJSON))
					.startHour(getInt(LOCATION_START_HOUR, calendarElementJSON))
					.startMinute(getInt(LOCATION_START_MINUTE, calendarElementJSON))
					.endHour(getInt(LOCATION_END_HOUR, calendarElementJSON))
					.endMinute(getInt(LOCATION_END_MINUTE, calendarElementJSON))
					.partialBusinessHoursMap(getPartialBusinessHours(
							(JSONArray) calendarElementJSON.get(PARTIAL_BUSINESS_HOURS_DAYS_ELEMENT)))
					.build());
		}
	}

	private Map<LocalDate, PartialBusinessHour> getPartialBusinessHours(JSONArray jsonArray)
			throws CalendarElementNotFound {

		if (jsonArray == null)
			return null;

		Map<LocalDate, PartialBusinessHour> partialBusinessHours = new LinkedHashMap<>();

		for (Object partialBusinessHourElement : jsonArray) {

			JSONObject partialBusonessHourJSON = (JSONObject) partialBusinessHourElement;
			PartialBusinessHour partialBusinessHour = PartialBusinessHour.builder()
					.startHour(getInt(PARTIAL_BUSINESS_HOURS_DAYS_START_HOUR_ELEMENT, partialBusonessHourJSON))
					.startMinute(getInt(PARTIAL_BUSINESS_HOURS_DAYS_START_MINUTE_ELEMENT, partialBusonessHourJSON))
					.endHour(getInt(PARTIAL_BUSINESS_HOURS_DAYS_END_HOUR_ELEMENT, partialBusonessHourJSON))
					.endMinute(getInt(PARTIAL_BUSINESS_HOURS_DAYS_END_MINUTE_ELEMENT, partialBusonessHourJSON)).build();

			partialBusinessHours.put(
					LocalDate.parse(getString(PARTIAL_BUSINESS_HOURS_DAYS_DATE_ELEMENT, partialBusonessHourJSON)),
					partialBusinessHour);
		}

		return partialBusinessHours;
	}

	private void addNonBusinessHoursDays(Calendar calendar) throws CalendarElementNotFound, InvalidCalendarException {
		for (Object nonBusinessDayElement : nonBusinessHoursDays) {

			JSONObject nonBusinessDayElementJSON = (JSONObject) nonBusinessDayElement;

			List<String> dates = getList(NON_BUSINESS_DAYS, nonBusinessDayElementJSON);

			calendar.addNonBusinessDaysByLocation(getString(LOCATION_NODE, nonBusinessDayElementJSON),
					dates.stream().map(o -> LocalDate.parse(String.valueOf(o))).collect(Collectors.toList()));
		}
	}

	private void addDSTInfo(Calendar calendar) throws CalendarElementNotFound, InvalidCalendarException {
		for (Object dstInfo : dstInfoList) {

			JSONObject dstElementJSON = (JSONObject) dstInfo;

			calendar.addDayLightSavingInfoByLocation(getString(LOCATION_NODE, dstElementJSON),
					List.of(DayLightSavingInfo.builder()

							.start(LocalDateTime.parse(getString(DST_START, dstElementJSON)))
							.end(LocalDateTime.parse(getString(DST_END, dstElementJSON)))
							.offsetInMinutes(getInt(DST_OFFSET, dstElementJSON))

							.build()));
		}

	}

	private void validateCalendarDataSource(JSONObject calendarDataSource) throws InvalidCalendarException {
		if (Objects.isNull(calendarDataSource)) {
			throw new InvalidCalendarException("Null Calendar Data Source");
		}

		if (Objects.isNull(calendarDataSource.get(REGULAR_BUSINESS_HOURS))
				|| !(calendarDataSource.get(REGULAR_BUSINESS_HOURS) instanceof List)) {
			throw new InvalidCalendarException("Calendars element not found in Data Source");
		}

	}

	private String getString(String elementName, Map<String, Object> jsonObject) throws CalendarElementNotFound {

		Object jsonElementObject = jsonObject.get(elementName);

		if (jsonElementObject == null) {
			throw new CalendarElementNotFound(elementName);
		}

		return String.valueOf(jsonElementObject);
	}

	private int getInt(String elementName, Map<String, Object> jsonObject) throws CalendarElementNotFound {
		Object jsonElementObject = jsonObject.get(elementName);

		if (jsonElementObject == null) {
			throw new CalendarElementNotFound(elementName);
		}

		return Integer.valueOf(String.valueOf(jsonElementObject));
	}

	private List<String> getList(String elementName, Map<String, Object> jsonObject)
			throws CalendarElementNotFound, InvalidCalendarException {

		Object jsonElementObject = jsonObject.get(elementName);

		if (jsonElementObject == null) {
			throw new CalendarElementNotFound(elementName);
		}

		if (!(jsonElementObject instanceof Collection)) {
			throw new InvalidCalendarException("Error while converting element into Array type");
		}

		Collection<String> jsonElementAsCollection = (Collection) jsonElementObject;
		return jsonElementAsCollection.stream().collect(Collectors.toList());
	}

}
