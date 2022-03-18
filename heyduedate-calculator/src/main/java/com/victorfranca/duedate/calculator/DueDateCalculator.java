package com.victorfranca.duedate.calculator;

import static com.victorfranca.duedate.calculator.Dates.addMinutes;
import static com.victorfranca.duedate.calculator.Dates.diffInMinutes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.victorfranca.duedate.calculator.daylightsaving.DayLightSavingVisitor;
import com.victorfranca.duedate.calculator.log.CalculationLog;
import com.victorfranca.duedate.calculator.log.CalculationLogBlock;
import com.victorfranca.duedate.calculator.nonbusinesshour.NonBusinessDayVisitor;
import com.victorfranca.duedate.calculator.overlap.OverlapCalendarRule;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

//TODO trim seconds?
//TODO different time zones
//TODO Overlaping location times scenarios
//TODO Thread safe? Concurrency?
//TODO class and methods comments
/**
 * @author victor.franca
 * 
 */
public class DueDateCalculator {

	private List<CalculatorBlock> currentDateCalendarBlocks;

	private List<CalculatorBlock> unmergedCalendarBlocks;

	private long currentDateOnDurationInMinutes;

	private LocalDateTime currentDateStartDateTime;

	private long slaCounterInMinutes = 0;

	private OverlapCalendarRule overlapCalendarRule;

	public DueDateCalculator() {
		this.overlapCalendarRule = OverlapCalendarRule.builder().build();
	}

	public CalculationLog calculateDueDateWithLog(Calendar calendar, LocalDateTime startDateTime, long slaInMinutes) {
		CalculationLog calculationLog = new CalculationLog();

		LocalDateTime dueDateTime = calculateDueDate(calendar, startDateTime, slaInMinutes, calculationLog);

		if (calculationLog != null && !calculationLog.isEmpty()) {
			calculationLog.setStartDateTime(startDateTime);
			calculationLog.setDueDateTime(dueDateTime);
			calculationLog.truncateTimeUsedBySLA();
		}

		return calculationLog;

	}

	// TODO exception when sla rolls over calendarDay onDuration
	// Exception when SLA == ZERO
	public CalculationLog calculateDueDate(Calendar calendar, LocalDateTime startDateTime, long slaInMinutes) {
		return new CalculationLog(calculateDueDate(calendar, startDateTime, slaInMinutes, null));
	}

	private LocalDateTime calculateDueDate(Calendar calendar, LocalDateTime startDateTime, long slaInMinutes,
			CalculationLog calculationLog) {

		initCalendarBlocks(calendar, startDateTime, calculationLog);
		this.slaCounterInMinutes = slaInMinutes;
		this.currentDateStartDateTime = startDateTime;

		advanceToDueDateDay(calendar, slaInMinutes, calculationLog);

		CalculatorBlock calendarBlock = getDueDateCalendarBlockAndUpdateSLACounter();
		LocalDateTime dueDateTime = addMinutes(Long.valueOf(slaCounterInMinutes).intValue(), calendarBlock.getEnd());

		return dueDateTime;
	}

	// TODO refactor: avoid side effect behavior(get block and updating global
	// variable)
	private CalculatorBlock getDueDateCalendarBlockAndUpdateSLACounter() {
		Iterator<CalculatorBlock> calendarBlockIterator = this.currentDateCalendarBlocks.iterator();
		CalculatorBlock calendarBlock = null;
		while (slaCounterInMinutes > 0) {
			calendarBlock = calendarBlockIterator.next();

			// TODO implement calendarBlock.next() / calendarBlock.nextON()
			if (calendarBlock.isOn()) {
				if ((!currentDateStartDateTime.isAfter(calendarBlock.getEnd()))) {
					slaCounterInMinutes -= calendarBlock.getDurationInMinutes();
					if (currentDateStartDateTime.isAfter(calendarBlock.getStart())) {
						long minutesDiff = diffInMinutes(currentDateStartDateTime, calendarBlock.getStart());
						slaCounterInMinutes += minutesDiff;
					}
				}
			}
		}

		return calendarBlock;
	}

	private void advanceToDueDateDay(Calendar calendar, long slaInMinutes, CalculationLog calculationLog) {
		// TODO refactor: move to a data structure (iterator?)
		long rollingSlaMinutes = getRollingSlaMinutes(currentDateStartDateTime, slaInMinutes);
		while (rollingSlaMinutes > 0) {
			incCalendarBlocksDay(calendar, calculationLog);
			if (rollingSlaMinutes >= 0) {
				slaCounterInMinutes = rollingSlaMinutes;
				currentDateStartDateTime = this.currentDateCalendarBlocks.get(0).getStart();
			}
			rollingSlaMinutes = getRollingSlaMinutes(currentDateStartDateTime, rollingSlaMinutes);
		}
	}

	private void initCalendarBlocks(Calendar calendar, LocalDateTime startDateTime, CalculationLog calculationLog) {

		this.currentDateCalendarBlocks = new ArrayList<>();

		for (LocationRegularBusinessHours locationRegularBusinessHours : calendar.getRegularBusinessHours()) {

			LocalDateTime start = startDateTime
					.withHour(locationRegularBusinessHours.getStartHour(startDateTime.toLocalDate()))
					.withMinute(locationRegularBusinessHours.getStartMinute(startDateTime.toLocalDate()))
					.truncatedTo(ChronoUnit.MINUTES);

			LocalDateTime end = startDateTime
					.withHour(locationRegularBusinessHours.getEndHour(startDateTime.toLocalDate()))
					.withMinute(locationRegularBusinessHours.getEndMinute(startDateTime.toLocalDate()))
					.truncatedTo(ChronoUnit.MINUTES);

			CalculatorBlock calendarBlock = new CalculatorBlock(
					locationRegularBusinessHours.getLocation(startDateTime.toLocalDate()), start, end);

			addToCurrentCalendarBlocks(calendar, calendarBlock, calculationLog);
		}

		unmergedCalendarBlocks = new ArrayList<>(currentDateCalendarBlocks);
		currentDateCalendarBlocks = overlapCalendarRule.applyTo(currentDateCalendarBlocks);

		buildCalculationLog(calculationLog);

		updateOnDurationInMinutes();
	}

	private void addToCurrentCalendarBlocks(Calendar calendar, CalculatorBlock calendarBlock,
			CalculationLog calculationLog) {
		this.currentDateCalendarBlocks.add(calendarBlock);

		calendarBlock.accept(DayLightSavingVisitor.builder(calendar.getDayLightSavingInfoByLocation()).build());

		calendarBlock.accept(NonBusinessDayVisitor.builder(calendar.getNonBusinessDaysByLocation()).build());
	}

	private long getRollingSlaMinutes(LocalDateTime startDateTime, long slaInMinutes) {
		return slaInMinutes - getAdaptedOnDurationInMinutes(startDateTime);
	}

	private long getAdaptedOnDurationInMinutes(LocalDateTime startDateTime) {
		long adaptedOnDurationInMinutes = currentDateOnDurationInMinutes;

		for (Iterator<CalculatorBlock> iterator = currentDateCalendarBlocks.iterator(); iterator.hasNext();) {
			CalculatorBlock calendarBlock = (CalculatorBlock) iterator.next();
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

	private void incCalendarBlocksDay(Calendar calendar, CalculationLog calculationLog) {

		unmergedCalendarBlocks.forEach(o -> o.nextDay());

		unmergedCalendarBlocks.forEach(
				o -> o.accept(DayLightSavingVisitor.builder(calendar.getDayLightSavingInfoByLocation()).build()));

		unmergedCalendarBlocks
				.forEach(o -> o.accept(NonBusinessDayVisitor.builder(calendar.getNonBusinessDaysByLocation()).build()));

		currentDateCalendarBlocks.clear();
		currentDateCalendarBlocks.addAll(unmergedCalendarBlocks);

		currentDateCalendarBlocks = overlapCalendarRule.applyTo(currentDateCalendarBlocks);

		updateOnDurationInMinutes();

		buildCalculationLog(calculationLog);

	}

	private void updateOnDurationInMinutes() {
		currentDateOnDurationInMinutes = 0;
		currentDateCalendarBlocks.stream().filter(o -> o.isOn())
				.forEach(o -> currentDateOnDurationInMinutes += o.getDurationInMinutes());
	}

	private void buildCalculationLog(CalculationLog calculationLog) {
		if (calculationLog != null) {
			currentDateCalendarBlocks.forEach(o -> calculationLog.add(new CalculationLogBlock(o.getStart(), o.getEnd(),
					o.getDurationInMinutes(), o.isOn(), o.isDstAffected(), o.getLocationId())));
		}
	}

}
