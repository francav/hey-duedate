package com.victorfranca.duedate.calendar;

import java.time.LocalDate;
import java.util.Map;

import com.victorfranca.duedate.calculator.partialbusinesshours.PartialBusinessHour;

import lombok.Builder;

//TODO multiple business hours windows by location
/**
 * @author victor.franca
 *
 */
@Builder
public class LocationRegularBusinessHours {

	private String location;

	@Builder.Default
	private int startHour = -1;

	@Builder.Default
	private int startMinute = -1;

	@Builder.Default
	private int endHour = -1;

	@Builder.Default
	private int endMinute = -1;

	private Map<LocalDate, PartialBusinessHour> partialBusinessHoursMap;

	public int getStartHour(LocalDate date) {
		if (partialBusinessHoursMap == null || partialBusinessHoursMap.isEmpty()
				|| partialBusinessHoursMap.get(date) == null) {
			return startHour;
		}

		return partialBusinessHoursMap.get(date).getStartHour();
	}

	public int getStartMinute(LocalDate date) {
		if (partialBusinessHoursMap == null || partialBusinessHoursMap.isEmpty()
				|| partialBusinessHoursMap.get(date) == null) {
			return startMinute;
		}

		return partialBusinessHoursMap.get(date).getStartMinute();
	}

	public int getEndHour(LocalDate date) {
		if (partialBusinessHoursMap == null || partialBusinessHoursMap.isEmpty()
				|| partialBusinessHoursMap.get(date) == null) {
			return endHour;
		}

		return partialBusinessHoursMap.get(date).getEndHour();
	}

	public int getEndMinute(LocalDate date) {
		if (partialBusinessHoursMap == null || partialBusinessHoursMap.isEmpty()
				|| partialBusinessHoursMap.get(date) == null) {
			return endMinute;
		}

		return partialBusinessHoursMap.get(date).getEndMinute();
	}
	
	public Map<LocalDate, PartialBusinessHour> getPartialBusinessHoursMap() {
		return partialBusinessHoursMap;
	}

	public String getLocation(LocalDate date) {
		return location;
	}
}
