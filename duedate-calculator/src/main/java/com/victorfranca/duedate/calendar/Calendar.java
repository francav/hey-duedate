package com.victorfranca.duedate.calendar;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Calendar {

	private List<LocationRegularBusinessHours> locationRegularBusinessHoursList;

	private Map<String, List<DayLightSavingInfo>> dayLightSavingInfoByLocation;

	private Map<String, List<LocalDate>> nonBusinessDaysByLocation;

}
