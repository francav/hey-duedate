package com.victorfranca.duedate.calendar.provider.json;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;
import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingInfo;
import com.victorfranca.duedate.calendar.provider.spi.CalendarDataSourceElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.CalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.InvalidCalendarDataSourceException;

public class JSONCalendarProvider implements CalendarProvider {

	private JSONArray regularBusinessHoursDays;
	private JSONArray nonBusinessHoursDays;
	private JSONArray dstInfoList;

	private static final String REGULAR_BUSINESS_HOURS = "regularBusinessHours";
	private static final String LOCATION_NODE = "location";
	private static final String LOCATION_START_HOUR = "startHour";
	private static final String LOCATION_START_MINUTE = "startMinute";
	private static final String LOCATION_END_HOUR = "endHour";
	private static final String LOCATION_END_MINUTE = "endMinute";

	private static final String NON_BUSINESS_HOURS_DAYS = "nonBusinessHoursDays";
	private static final String NON_BUSINESS_DAYS = "value";

	private static final String DST_INFO_LIST = "daylightSavingTimeInfo";
	private static final String DST_START = "start";
	private static final String DST_END = "end";
	private static final String DST_OFFSET = "offsetInMinutes";

	@SuppressWarnings("unused")
	private JSONCalendarProvider() {
	}

	public JSONCalendarProvider(JSONObject calendarDataSource) throws InvalidCalendarDataSourceException {
		try {
			validateCalendarDataSource(calendarDataSource);
		} catch (InvalidCalendarDataSourceException e) {
			throw e;
		}

		this.regularBusinessHoursDays = (JSONArray) calendarDataSource.get(REGULAR_BUSINESS_HOURS);
		this.nonBusinessHoursDays = (JSONArray) calendarDataSource.get(NON_BUSINESS_HOURS_DAYS);
		this.dstInfoList = (JSONArray) calendarDataSource.get(DST_INFO_LIST);
	}

	@Override
	public Calendar createCalendar() throws CalendarDataSourceElementNotFound, InvalidCalendarDataSourceException {
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

	private void addLocationRegularBusinessHours(Calendar calendar) throws CalendarDataSourceElementNotFound {
		for (Object calendarElement : regularBusinessHoursDays) {

			JSONObject calendarElementJSON = (JSONObject) calendarElement;

			calendar.addLocationRegularBusinessHours(LocationRegularBusinessHours.builder()

					.location(getString(LOCATION_NODE, calendarElementJSON))
					.startHour(getInt(LOCATION_START_HOUR, calendarElementJSON))
					.startMinute(getInt(LOCATION_START_MINUTE, calendarElementJSON))
					.endHour(getInt(LOCATION_END_HOUR, calendarElementJSON))
					.endMinute(getInt(LOCATION_END_MINUTE, calendarElementJSON))

					.build());
		}
	}

	private void addNonBusinessHoursDays(Calendar calendar)
			throws CalendarDataSourceElementNotFound, InvalidCalendarDataSourceException {
		for (Object nonBusinessDayElement : nonBusinessHoursDays) {

			JSONObject nonBusinessDayElementJSON = (JSONObject) nonBusinessDayElement;

			List<String> dates = getList(NON_BUSINESS_DAYS, nonBusinessDayElementJSON);

			calendar.addNonBusinessDaysByLocation(getString(LOCATION_NODE, nonBusinessDayElementJSON),
					dates.stream().map(o -> LocalDate.parse(String.valueOf(o))).collect(Collectors.toList()));
		}
	}

	private void addDSTInfo(Calendar calendar)
			throws CalendarDataSourceElementNotFound, InvalidCalendarDataSourceException {
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

	private void validateCalendarDataSource(JSONObject calendarDataSource) throws InvalidCalendarDataSourceException {
		if (Objects.isNull(calendarDataSource)) {
			throw new InvalidCalendarDataSourceException("Null Calendar Data Source");
		}

		if (Objects.isNull(calendarDataSource.get(REGULAR_BUSINESS_HOURS))
				|| !(calendarDataSource.get(REGULAR_BUSINESS_HOURS) instanceof JSONArray)) {
			throw new InvalidCalendarDataSourceException("Calendars element not found in Data Source");
		}

		// TODO implement JSON calendar data source validation
	}

	private String getString(String elementName, JSONObject jsonObject) throws CalendarDataSourceElementNotFound {

		Object jsonElementObject = jsonObject.get(elementName);

		if (jsonElementObject == null) {
			throw new CalendarDataSourceElementNotFound(elementName);
		}

		return String.valueOf(jsonElementObject);
	}

	private int getInt(String elementName, JSONObject jsonObject) throws CalendarDataSourceElementNotFound {
		Object jsonElementObject = jsonObject.get(elementName);

		if (jsonElementObject == null) {
			throw new CalendarDataSourceElementNotFound(elementName);
		}

		return Integer.valueOf(String.valueOf(jsonElementObject));
	}

	private List getList(String elementName, JSONObject jsonObject)
			throws CalendarDataSourceElementNotFound, InvalidCalendarDataSourceException {
		Object jsonElementObject = jsonObject.get(elementName);

		if (jsonElementObject == null) {
			throw new CalendarDataSourceElementNotFound(elementName);
		}

		if (!(jsonElementObject instanceof Collection)) {
			throw new InvalidCalendarDataSourceException("Error while converting element into Array type");
		}

		Collection<String> jsonElementAsCollection = (Collection) jsonElementObject;
		return jsonElementAsCollection.stream().collect(Collectors.toList());
	}

}
