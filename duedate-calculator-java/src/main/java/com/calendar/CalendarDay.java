package com.calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.calendar.daylightsaving.DayLightSavingProviderFactory;
import com.calendar.daylightsaving.DayLightSavingVisitorFactory;
import com.calendar.nonbusinesshour.NonBusinessDayProviderFactory;
import com.calendar.nonbusinesshour.NonBusinessDayVisitorFactory;
import com.util.DateUtil;

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
					adaptedOnDurationInMinutes -= DateUtil.diffInMinutes(startDateTime, calendarBlock.getStart())
							+ DateUtil.diffInMinutes(calendarBlock.getEnd(), startDateTime);
				}
				if (!startDateTime.isBefore(calendarBlock.getStart())
						&& !startDateTime.isAfter(calendarBlock.getEnd())) {
					adaptedOnDurationInMinutes += DateUtil.diffInMinutes(calendarBlock.getEnd(), startDateTime);
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
