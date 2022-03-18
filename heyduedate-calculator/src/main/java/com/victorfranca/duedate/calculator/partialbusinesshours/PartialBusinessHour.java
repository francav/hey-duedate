package com.victorfranca.duedate.calculator.partialbusinesshours;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PartialBusinessHour {

	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;

}
