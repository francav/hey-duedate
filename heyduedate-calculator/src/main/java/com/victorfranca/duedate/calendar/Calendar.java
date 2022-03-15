package com.victorfranca.duedate.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calculator.daylightsaving.DayLightSavingInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author victor.franca
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class Calendar {

	private List<LocationRegularBusinessHours> regularBusinessHours = new ArrayList<>();

	private Map<String, List<LocalDate>> nonBusinessDaysByLocation = new LinkedHashMap<>();

	private Map<String, List<DayLightSavingInfo>> dayLightSavingInfoByLocation = new LinkedHashMap<>();

	public void addLocationRegularBusinessHours(LocationRegularBusinessHours locationRegularBusinessHours) {
		this.regularBusinessHours.add(locationRegularBusinessHours);
	}

	public void addNonBusinessDaysByLocation(String location, List<LocalDate> nonBusinessDays) {
		this.nonBusinessDaysByLocation.put(location, nonBusinessDays);
	}

	public void addDayLightSavingInfoByLocation(String location,
			List<DayLightSavingInfo> dayLightSavingInfoByLocation) {
		this.dayLightSavingInfoByLocation.put(location, dayLightSavingInfoByLocation);
	}

}
