package com.victorfranca.duedate.calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.victorfranca.duedate.Dates;
import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingProviderFactory;
import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingVisitorFactory;
import com.victorfranca.duedate.calendar.nonbusinesshour.NonBusinessDayProviderFactory;
import com.victorfranca.duedate.calendar.nonbusinesshour.NonBusinessDayVisitorFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//TODO do we need Calendar and CalendarDay?
public class CalendarDay {

	private List<CalendarBlock> calendarBlocks = new ArrayList<CalendarBlock>();

	private long onDurationInMinutes;

	private NonBusinessDayProviderFactory nonBusinessDaysProviderFactory;

	private DayLightSavingProviderFactory dayLightSavingProviderFactory;

	public CalendarDay() {
	}

	public List<CalendarBlock> getCalendarBlocks() {
		return calendarBlocks;
	}

	public CalendarDay addCalendarBlock(CalendarBlock calendarBlock) {
		calendarBlocks.add(calendarBlock);

		calendarBlock.accept(DayLightSavingVisitorFactory.getDayLightSavingVisitor(dayLightSavingProviderFactory));

		// TODO consider DST and Non business hours mixed scenarious
		calendarBlock.accept(NonBusinessDayVisitorFactory.getNonBusinessDayVisitor(nonBusinessDaysProviderFactory));

		if (calendarBlock.isOn()) {
			onDurationInMinutes += calendarBlock.getDurationInMinutes();
		}

		return this;
	}

	public long getRollingSlaMinutes(LocalDateTime startDateTime, long slaInMinutes) {
		return slaInMinutes - getAdaptedOnDurationInMinutes(startDateTime);
	}

	public long getAdaptedOnDurationInMinutes(LocalDateTime startDateTime) {
		long adaptedOnDurationInMinutes = onDurationInMinutes;

		for (Iterator<CalendarBlock> iterator = calendarBlocks.iterator(); iterator.hasNext();) {
			CalendarBlock calendarBlock = (CalendarBlock) iterator.next();
			if (calendarBlock.isOn()) {
				if (!startDateTime.isBefore(calendarBlock.getStart())) {
					adaptedOnDurationInMinutes -= Dates.diffInMinutes(startDateTime, calendarBlock.getStart())
							+ Dates.diffInMinutes(calendarBlock.getEnd(), startDateTime);
				}
				if (!startDateTime.isBefore(calendarBlock.getStart())
						&& !startDateTime.isAfter(calendarBlock.getEnd())) {
					adaptedOnDurationInMinutes += Dates.diffInMinutes(calendarBlock.getEnd(), startDateTime);
				}
			}

		}

		return adaptedOnDurationInMinutes;
	}

	public void incCalendarBlocksDay() {
		calendarBlocks.forEach(o -> o.nextDay());

		calendarBlocks.forEach(
				o -> o.accept(DayLightSavingVisitorFactory.getDayLightSavingVisitor(dayLightSavingProviderFactory)));

		calendarBlocks.forEach(
				o -> o.accept(NonBusinessDayVisitorFactory.getNonBusinessDayVisitor(nonBusinessDaysProviderFactory)));

		updateOnDurationInMinutes();
	}

	private void updateOnDurationInMinutes() {
		onDurationInMinutes = 0;
		calendarBlocks.stream().filter(o -> o.isOn()).forEach(o -> onDurationInMinutes += o.getDurationInMinutes());
	}

}
