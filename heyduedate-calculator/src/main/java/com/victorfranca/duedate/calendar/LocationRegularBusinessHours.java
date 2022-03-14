package com.victorfranca.duedate.calendar;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

//TODO multiple business hours windows by location
/**
 * @author victor.franca
 *
 */
@Builder
@Getter
@Setter
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

}
