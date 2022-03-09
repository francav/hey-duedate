package com.victorfranca.duedate.calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.victorfranca.duedate.Dates;
import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingVisitor;
import com.victorfranca.duedate.calendar.nonbusinesshour.NonBusinessDayVisitor;
import com.victorfranca.duedate.calendar.provider.DayLightSavingProvider;
import com.victorfranca.duedate.calendar.provider.NonBusinessDayProvider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Calendar {

	private List<CalendarBlock> calendarBlocks = new ArrayList<CalendarBlock>();

	private long onDurationInMinutes;

	private NonBusinessDayProvider nonBusinessDaysProvider;

	private DayLightSavingProvider dayLightSavingProvider;

	public Calendar() {
	}

	public List<CalendarBlock> getCalendarBlocks() {
		return calendarBlocks;
	}

	public Calendar addCalendarBlock(CalendarBlock calendarBlock) {
		calendarBlocks.add(calendarBlock);

		calendarBlock.accept(DayLightSavingVisitor.builder(dayLightSavingProvider).build());

		// TODO consider DST and Non business hours mixed scenarious
		calendarBlock.accept(NonBusinessDayVisitor.builder(nonBusinessDaysProvider).build());

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

		calendarBlocks.forEach(o -> o.accept(DayLightSavingVisitor.builder(dayLightSavingProvider).build()));

		calendarBlocks.forEach(o -> o.accept(NonBusinessDayVisitor.builder(nonBusinessDaysProvider).build()));

		updateOnDurationInMinutes();
	}

	private void updateOnDurationInMinutes() {
		onDurationInMinutes = 0;
		calendarBlocks.stream().filter(o -> o.isOn()).forEach(o -> onDurationInMinutes += o.getDurationInMinutes());
	}
	
}
