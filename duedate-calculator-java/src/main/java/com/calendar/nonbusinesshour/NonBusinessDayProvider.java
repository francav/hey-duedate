package com.calendar.nonbusinesshour;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NonBusinessDayProvider {

	private Map<String, List<LocalDate>> nonBusinessDaysByLocation;

}
