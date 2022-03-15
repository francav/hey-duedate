package com.victorfranca.duedate.calculator.daylightsaving;

import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calculator.CalculatorBlock;
import com.victorfranca.duedate.calculator.CalculatorBlockVisitor;
import com.victorfranca.duedate.calculator.Dates;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * @author victor.franca
 *
 */
@AllArgsConstructor
@Builder(builderMethodName = "hiddenBuilder")
public class DayLightSavingVisitor implements CalculatorBlockVisitor {

	private Map<String, List<DayLightSavingInfo>> dayLightSavingInfoByLocation;

	public static DayLightSavingVisitorBuilder builder(
			Map<String, List<DayLightSavingInfo>> dayLightSavingInfoByLocation) {
		return hiddenBuilder().dayLightSavingInfoByLocation(dayLightSavingInfoByLocation);
	}

	@Override
	public void visit(CalculatorBlock calendarBlock) {

		if (dayLightSavingInfoByLocation != null) {
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

	private void updateStartDateForDST(CalculatorBlock calendarBlock, DayLightSavingInfo dayLightSavingInfo) {
		if (Dates.isBetween(dayLightSavingInfo.getStart(), dayLightSavingInfo.getEnd(), calendarBlock.getStart())) {
			calendarBlock.setStart(Dates.addMinutes(1 * dayLightSavingInfo.getOffsetInMinutes(), calendarBlock.getStart()));
			calendarBlock.setDstAffected(true);
		}
	}

	private void updateEndDateForDST(CalculatorBlock calendarBlock, DayLightSavingInfo dayLightSavingInfo) {
		if (Dates.isBetween(dayLightSavingInfo.getStart(), dayLightSavingInfo.getEnd(), calendarBlock.getEnd())) {
			calendarBlock.setEnd(Dates.addMinutes(1 * dayLightSavingInfo.getOffsetInMinutes(), calendarBlock.getEnd()));
			calendarBlock.setDstAffected(true);
		}
	}

}
