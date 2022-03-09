package com.victorfranca.duedate.calculator;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import com.victorfranca.duedate.Dates;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.CalendarBlock;
import com.victorfranca.duedate.calendar.provider.CalendarProvider;

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
//TODO Thread safe?
public class DueDateCalculator {

	private CalendarProvider calendarProvider;

	// TODO is it ok to keep this is a global variable? Smells bad
	private Calendar calendar;

	@SuppressWarnings("unused")
	private DueDateCalculator() {
	}

	public DueDateCalculator(CalendarProvider calendarProvider) {

		if (calendarProvider == null || calendarProvider.getCalendar() == null) {
			throw new IllegalCalendarProviderException();
		}

		this.calendarProvider = calendarProvider;
		this.calendar = calendarProvider.getCalendar();
	}

	// TODO exception when sla rolls over calendarDay onDuration
	// Exception when SLA == ZERO
	public LocalDateTime calculateDueDate(LocalDateTime startDateTime, long slaInMinutes) {

		// TODO refactor: move to a data structure (iterator?)
		long slaCounterInMinutes = slaInMinutes;
		long rollingSlaMinutes = calendar.getRollingSlaMinutes(startDateTime, slaInMinutes);
		while (rollingSlaMinutes > 0) {
			calendar.incCalendarBlocksDay();
			if (rollingSlaMinutes >= 0) {
				slaCounterInMinutes = rollingSlaMinutes;
				startDateTime = calendar.getCalendarBlocks().get(0).getStart();
			}
			rollingSlaMinutes = calendar.getRollingSlaMinutes(startDateTime, rollingSlaMinutes);
		}

		List<CalendarBlock> calendarBlocks = calendar.getCalendarBlocks();
		Iterator<CalendarBlock> calendarBlockIterator = calendarBlocks.iterator();
		CalendarBlock calendarBlock = null;
		while (slaCounterInMinutes > 0) {
			calendarBlock = calendarBlockIterator.next();

			// TODO implement calendarBlock.next() / calendarBlock.nextON()
			if (calendarBlock.isOn()) {
				if ((!startDateTime.isAfter(calendarBlock.getEnd()))) {
					slaCounterInMinutes -= calendarBlock.getDurationInMinutes();
					if (startDateTime.isAfter(calendarBlock.getStart())) {
						long minutesDiff = Dates.diffInMinutes(startDateTime, calendarBlock.getStart());
						slaCounterInMinutes += minutesDiff;
					}
				}
			}
		}

		return Dates.addMinutes(Long.valueOf(slaCounterInMinutes).intValue(), calendarBlock.getEnd());
	}

}
