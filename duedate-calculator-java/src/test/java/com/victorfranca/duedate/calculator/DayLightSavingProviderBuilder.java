package com.victorfranca.duedate.calculator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingInfo;
import com.victorfranca.duedate.calendar.provider.spi.DayLightSavingProvider;

public final class DayLightSavingProviderBuilder {

	public static DayLightSavingProvider createDayLightSavingProvider(String location, LocalDateTime start,
			LocalDateTime end, int offset) {

		DayLightSavingProvider dstProviderFactory = new DayLightSavingProvider() {

			@Override
			public Map<String, List<DayLightSavingInfo>> getDayLightSavingInfoByLocation() {
				Map<String, List<DayLightSavingInfo>> dstMap = new LinkedHashMap<>();

				List<DayLightSavingInfo> dstInfoList = new ArrayList<>();

				dstInfoList.add(DayLightSavingInfo.builder().start(start).end(end).offsetInMinutes(offset).build());

				dstMap.put(location, dstInfoList);

				return dstMap;
			}
		};

		return dstProviderFactory;

	}

}
