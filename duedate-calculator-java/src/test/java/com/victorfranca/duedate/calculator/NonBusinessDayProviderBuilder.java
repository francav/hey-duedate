package com.victorfranca.duedate.calculator;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calendar.provider.spi.NonBusinessDayProvider;

public final class NonBusinessDayProviderBuilder {

	public static NonBusinessDayProvider createNonBusinessDayProvider(String location1, LocalDate date1,
			String location2, LocalDate date2) {

		return new NonBusinessDayProvider() {

			@Override
			public Map<String, List<LocalDate>> getNonBusinessDaysByLocation() {
				Map<String, List<LocalDate>> nonBusinessDaysMap = new LinkedHashMap<>();
				nonBusinessDaysMap.put(location1, List.of(date1));
				if (location2 != null && date2 != null) {
					nonBusinessDaysMap.put(location2, List.of(date2));
				}

				return nonBusinessDaysMap;
			}
		};

	}

}
