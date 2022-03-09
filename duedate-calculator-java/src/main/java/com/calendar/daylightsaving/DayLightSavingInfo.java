package com.calendar.daylightsaving;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DayLightSavingInfo {

	private LocalDateTime start;
	private LocalDateTime end;

	private int offsetInMinutes;

}
