package com.victorfranca.duedate.calendar;

import lombok.Builder;
import lombok.Getter;

//TODO location timezone
//TODO multiple business hours windows by location
@Builder
@Getter
public class LocationRegularBusinessHours {

	private String locationID;

	private int startHour;
	private int startMinute;

	private int endHour;
	private int endMinute;

}
