package com.victorfranca.duedate.calculator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingInfo;
import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingProvider;
import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingProviderFactory;

public final class DayLightSavingProviderFactoryBuilder {

	public static DayLightSavingProviderFactory createDayLightSavingProviderFactory(String location, LocalDateTime start,
			LocalDateTime end, int offset) {

		Map<String, List<DayLightSavingInfo>> dstMap = new LinkedHashMap<>();

		List<DayLightSavingInfo> dstInfoList = new ArrayList<>();

		dstInfoList.add(DayLightSavingInfo.builder().start(start)
				.end(end).offsetInMinutes(offset).build());

		dstMap.put(location, dstInfoList);

		DayLightSavingProviderFactory dstProviderFactory = new DayLightSavingProviderFactory() {
			@Override
			public DayLightSavingProvider getDayLightSavingProvider() {
				return new DayLightSavingProvider(dstMap);
			}
		};
		
		return dstProviderFactory;

	}

}
