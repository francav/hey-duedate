package com.victorfranca.duedate.calendar.daylightsaving;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author victor.franca
 *
 */
@Getter
@Setter
@Builder
public class DayLightSavingInfo {

	private LocalDateTime start;
	private LocalDateTime end;

	private int offsetInMinutes;

}
