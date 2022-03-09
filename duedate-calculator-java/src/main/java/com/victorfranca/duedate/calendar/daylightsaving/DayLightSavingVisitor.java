package com.victorfranca.duedate.calendar.daylightsaving;

import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.Dates;
import com.victorfranca.duedate.calendar.CalendarBlock;
import com.victorfranca.duedate.calendar.CalendarBlockVisitor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DayLightSavingVisitor implements CalendarBlockVisitor {

	private DayLightSavingProviderFactory dayLightSavingProviderFactory;

	@Override
	public void visit(CalendarBlock calendarBlock) {

		if (dayLightSavingProviderFactory != null) {
			Map<String, List<DayLightSavingInfo>> dayLightSavingInfoByLocation = dayLightSavingProviderFactory
					.getDayLightSavingProvider().getDayLightSavingInfoByLocation();

			if (dayLightSavingInfoByLocation.containsKey(calendarBlock.getLocationId())) {

				List<DayLightSavingInfo> dayLightSavingInfoList = dayLightSavingInfoByLocation
						.get(calendarBlock.getLocationId());

				for (DayLightSavingInfo dayLightSavingInfo : dayLightSavingInfoList) {

					updateStartDateForDST(calendarBlock, dayLightSavingInfo);

					updateEndDateForDST(calendarBlock, dayLightSavingInfo);
				}
			}
		}

	}

	// TODO merge updateDateForDST methods
	private void updateStartDateForDST(CalendarBlock calendarBlock, DayLightSavingInfo dayLightSavingInfo) {
		if (Dates.isBetween(dayLightSavingInfo.getStart(), dayLightSavingInfo.getEnd(), calendarBlock.getStart())) {
			calendarBlock.setStart(Dates.addMinutes(dayLightSavingInfo.getOffsetInMinutes(), calendarBlock.getStart()));
		}
	}

	// TODO merge updateDateForDST methods
	private void updateEndDateForDST(CalendarBlock calendarBlock, DayLightSavingInfo dayLightSavingInfo) {
		if (Dates.isBetween(dayLightSavingInfo.getStart(), dayLightSavingInfo.getEnd(), calendarBlock.getEnd())) {
			calendarBlock.setEnd(Dates.addMinutes(1 * dayLightSavingInfo.getOffsetInMinutes(), calendarBlock.getEnd()));
		}
	}

}
