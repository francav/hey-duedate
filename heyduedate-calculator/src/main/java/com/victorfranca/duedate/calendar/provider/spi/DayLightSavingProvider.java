package com.victorfranca.duedate.calendar.provider.spi;

import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingInfo;

/**
 * @author victor.franca
 *
 */
public interface DayLightSavingProvider {

	public Map<String, List<DayLightSavingInfo>> getDayLightSavingInfoByLocation();

}
