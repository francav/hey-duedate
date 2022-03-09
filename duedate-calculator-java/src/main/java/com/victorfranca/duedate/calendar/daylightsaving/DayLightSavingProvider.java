package com.victorfranca.duedate.calendar.daylightsaving;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DayLightSavingProvider {

	private Map<String, List<DayLightSavingInfo>> dayLightSavingInfoByLocation;

}
