package com.victorfranca.duedate.calendar.nonbusinesshour;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calculator.Dates;
import com.victorfranca.duedate.calendar.CalendarBlock;
import com.victorfranca.duedate.calendar.CalendarBlockVisitor;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * @author victor.franca
 *
 */
@AllArgsConstructor
@Builder(builderMethodName = "hiddenBuilder")
public class NonBusinessDayVisitor implements CalendarBlockVisitor {

	private Map<String, List<LocalDate>> nonBusinessDaysByLocation;

	public static NonBusinessDayVisitorBuilder builder(Map<String, List<LocalDate>> nonBusinessDaysByLocation) {
		return hiddenBuilder().nonBusinessDaysByLocation(nonBusinessDaysByLocation);
	}

	@Override
	public void visit(CalendarBlock calendarBlock) {
		calendarBlock.setOn(!isNonBusinessHour(calendarBlock));
	}

	// TODO DST: comparing to calendar block start date may not work since end date
	// may fall in next day (unit test this)
	private boolean isNonBusinessHour(CalendarBlock calendarBlock) {

		if (nonBusinessDaysByLocation == null) {
			return false;
		} else {

			if (!nonBusinessDaysByLocation.containsKey(calendarBlock.getLocationId())) {
				return false;
			}

			return nonBusinessDaysByLocation.get(calendarBlock.getLocationId()).stream()
					.anyMatch(o -> Dates.isSameDay(o, calendarBlock.getStart().toLocalDate()));
		}
	}

}
