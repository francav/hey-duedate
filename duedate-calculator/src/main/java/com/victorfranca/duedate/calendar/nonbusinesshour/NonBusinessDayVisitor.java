package com.victorfranca.duedate.calendar.nonbusinesshour;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.Dates;
import com.victorfranca.duedate.calendar.CalendarBlock;
import com.victorfranca.duedate.calendar.CalendarBlockVisitor;
import com.victorfranca.duedate.calendar.provider.NonBusinessDayProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder(builderMethodName = "hiddenBuilder")
public class NonBusinessDayVisitor implements CalendarBlockVisitor {

	private NonBusinessDayProvider nonBusinessDaysProvider;

	public static NonBusinessDayVisitorBuilder builder(NonBusinessDayProvider nonBusinessDaysProvider) {
		return hiddenBuilder().nonBusinessDaysProvider(nonBusinessDaysProvider);
	}

	@Override
	public void visit(CalendarBlock calendarBlock) {
		calendarBlock.setOn(!isNonBusinessHour(calendarBlock));
	}

	// TODO DST: comparing to calendar block start date may not work since end date
	// may fall in next day (unit test this)
	private boolean isNonBusinessHour(CalendarBlock calendarBlock) {

		if (nonBusinessDaysProvider == null) {
			return false;
		} else {

			if (!nonBusinessDaysProvider.getNonBusinessDaysByLocation().containsKey(calendarBlock.getLocationId())) {
				return false;
			}

			Map<String, List<LocalDate>> nonBusinessDaysMap = nonBusinessDaysProvider.getNonBusinessDaysByLocation();

			return nonBusinessDaysMap.get(calendarBlock.getLocationId()).stream()
					.anyMatch(o -> Dates.isSameDay(o, calendarBlock.getStart().toLocalDate()));
		}
	}

}
