package com.calendar.nonbusinesshour;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.calendar.CalendarBlock;
import com.calendar.CalendarBlockVisitor;
import com.util.DateUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NonBusinessDayVisitor implements CalendarBlockVisitor {

	private NonBusinessDayProviderFactory nonBusinessDaysProviderFactory;

	@Override
	public void visit(CalendarBlock calendarBlock) {
		calendarBlock.setOn(!isNonBusinessHour(calendarBlock));
	}

	// TODO DST: comparing to calendar block start date may not work since end date
	// may fall in next day (unit test this)
	private boolean isNonBusinessHour(CalendarBlock calendarBlock) {

		if (nonBusinessDaysProviderFactory == null) {
			return false;
		} else {

			if (!nonBusinessDaysProviderFactory.getBusinessDaysProvider().getNonBusinessDaysByLocation()
					.containsKey(calendarBlock.getLocationId())) {
				return false;
			}

			Map<String, List<LocalDate>> nonBusinessDaysMap = nonBusinessDaysProviderFactory.getBusinessDaysProvider()
					.getNonBusinessDaysByLocation();

			return nonBusinessDaysMap.get(calendarBlock.getLocationId()).stream()
					.anyMatch(o -> DateUtil.isSameDay(o, calendarBlock.getStart().toLocalDate()));
		}
	}

}
