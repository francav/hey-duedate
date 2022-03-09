package com.calculator;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.calendar.nonbusinesshour.NonBusinessDayProvider;
import com.calendar.nonbusinesshour.NonBusinessDayProviderFactory;

public final class NonBusinessDayProviderFactoryBuilder {

	public static NonBusinessDayProviderFactory createNonBusinessDayProviderFactory(String location1, LocalDate date1,
			String location2, LocalDate date2) {

		Map<String, List<LocalDate>> nonBusinessDaysMap = new LinkedHashMap<>();
		nonBusinessDaysMap.put(location1, List.of(date1));
		if (location2 != null && date2 != null) {
			nonBusinessDaysMap.put(location2, List.of(date2));
		}

		return new NonBusinessDayProviderFactory() {
			public NonBusinessDayProvider getBusinessDaysProvider() {
				return new NonBusinessDayProvider(nonBusinessDaysMap);
			}
		};

	}

}
