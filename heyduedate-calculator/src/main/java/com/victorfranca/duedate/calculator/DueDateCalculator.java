package com.victorfranca.duedate.calculator;

import static com.victorfranca.duedate.calculator.Dates.addMinutes;
import static com.victorfranca.duedate.calculator.Dates.diffInMinutes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.CalendarBlock;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;
import com.victorfranca.duedate.calendar.daylightsaving.DayLightSavingVisitor;
import com.victorfranca.duedate.calendar.nonbusinesshour.NonBusinessDayVisitor;

/**
 * Scenario 1: startDateTime falls in first block start time
 * 
 * Scenario 2: startDateTime falls after first block start time
 * 
 * SLA always in minutes
 * 
 * @author a688992
 *
 */
//TODO trim seconds?
//TODO different time zones
//TODO Overlaping location times scenarios
//TODO Thread safe? Concurrency?
public class DueDateCalculator {

	private List<CalendarBlock> calendarBlocks;

	private long onDurationInMinutes;

	// TODO exception when sla rolls over calendarDay onDuration
	// Exception when SLA == ZERO
	public LocalDateTime calculateDueDate(Calendar calendar, LocalDateTime startDateTime, long slaInMinutes) {

		initCalendarBlocks(calendar, startDateTime);

		// TODO refactor: move to a data structure (iterator?)
		long slaCounterInMinutes = slaInMinutes;
		long rollingSlaMinutes = getRollingSlaMinutes(startDateTime, slaInMinutes);
		while (rollingSlaMinutes > 0) {
			incCalendarBlocksDay(calendar);
			if (rollingSlaMinutes >= 0) {
				slaCounterInMinutes = rollingSlaMinutes;
				startDateTime = this.calendarBlocks.get(0).getStart();
			}
			rollingSlaMinutes = getRollingSlaMinutes(startDateTime, rollingSlaMinutes);
		}

		Iterator<CalendarBlock> calendarBlockIterator = this.calendarBlocks.iterator();
		CalendarBlock calendarBlock = null;
		while (slaCounterInMinutes > 0) {
			calendarBlock = calendarBlockIterator.next();

			// TODO implement calendarBlock.next() / calendarBlock.nextON()
			if (calendarBlock.isOn()) {
				if ((!startDateTime.isAfter(calendarBlock.getEnd()))) {
					slaCounterInMinutes -= calendarBlock.getDurationInMinutes();
					if (startDateTime.isAfter(calendarBlock.getStart())) {
						long minutesDiff = diffInMinutes(startDateTime, calendarBlock.getStart());
						slaCounterInMinutes += minutesDiff;
					}
				}
			}
		}

		return addMinutes(Long.valueOf(slaCounterInMinutes).intValue(), calendarBlock.getEnd());
	}

	private void initCalendarBlocks(Calendar calendar, LocalDateTime startDateTime) {

		calendarBlocks = new ArrayList<>();

		for (LocationRegularBusinessHours locationRegularBusinessHours : calendar
				.getRegularBusinessHours()) {

			LocalDateTime start = startDateTime.withHour(locationRegularBusinessHours.getStartHour())
					.withMinute(locationRegularBusinessHours.getStartMinute()).truncatedTo(ChronoUnit.MINUTES);

			LocalDateTime end = startDateTime.withHour(locationRegularBusinessHours.getEndHour())
					.withMinute(locationRegularBusinessHours.getEndMinute()).truncatedTo(ChronoUnit.MINUTES);

			CalendarBlock calendarBlock = new CalendarBlock(locationRegularBusinessHours.getLocation(), start, end);

			addCalendarBlock(calendar, calendarBlock);
		}

	}

	private void addCalendarBlock(Calendar calendar, CalendarBlock calendarBlock) {
		this.calendarBlocks.add(calendarBlock);

		calendarBlock.accept(DayLightSavingVisitor.builder(calendar.getDayLightSavingInfoByLocation()).build());

		calendarBlock.accept(NonBusinessDayVisitor.builder(calendar.getNonBusinessDaysByLocation()).build());

		if (calendarBlock.isOn()) {
			onDurationInMinutes += calendarBlock.getDurationInMinutes();
		}

	}

	private long getRollingSlaMinutes(LocalDateTime startDateTime, long slaInMinutes) {
		return slaInMinutes - getAdaptedOnDurationInMinutes(startDateTime);
	}

	private long getAdaptedOnDurationInMinutes(LocalDateTime startDateTime) {
		long adaptedOnDurationInMinutes = onDurationInMinutes;

		for (Iterator<CalendarBlock> iterator = calendarBlocks.iterator(); iterator.hasNext();) {
			CalendarBlock calendarBlock = (CalendarBlock) iterator.next();
			if (calendarBlock.isOn()) {
				if (!startDateTime.isBefore(calendarBlock.getStart())) {
					adaptedOnDurationInMinutes -= diffInMinutes(startDateTime, calendarBlock.getStart())
							+ diffInMinutes(calendarBlock.getEnd(), startDateTime);
				}
				if (!startDateTime.isBefore(calendarBlock.getStart())
						&& !startDateTime.isAfter(calendarBlock.getEnd())) {
					adaptedOnDurationInMinutes += diffInMinutes(calendarBlock.getEnd(), startDateTime);
				}
			}

		}

		return adaptedOnDurationInMinutes;
	}

	private void incCalendarBlocksDay(Calendar calendar) {
		calendarBlocks.forEach(o -> o.nextDay());

		calendarBlocks.forEach(
				o -> o.accept(DayLightSavingVisitor.builder(calendar.getDayLightSavingInfoByLocation()).build()));

		calendarBlocks
				.forEach(o -> o.accept(NonBusinessDayVisitor.builder(calendar.getNonBusinessDaysByLocation()).build()));

		updateOnDurationInMinutes();
	}

	private void updateOnDurationInMinutes() {
		onDurationInMinutes = 0;
		calendarBlocks.stream().filter(o -> o.isOn()).forEach(o -> onDurationInMinutes += o.getDurationInMinutes());
	}

}
