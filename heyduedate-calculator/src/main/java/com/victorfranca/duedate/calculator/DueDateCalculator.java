/*
 * Copyright WKS Power Limited under one or more contributor license agreements. 
 * See the LICENSE file distributed with this work for additional information regarding 
 * copyright ownership. WKS Power licenses this file to you under the 
 * GNU AFFERO GENERAL PUBLIC LICENSE v3.0; you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/agpl-3.0.en.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.victorfranca.duedate.calculator.log.CalculationLogTimeTruncator;
import com.victorfranca.duedate.calculator.nonbusinesshour.NonBusinessDayVisitor;
import com.victorfranca.duedate.calculator.overlap.OverlapCalendarRule;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

/**
 * @author victor.franca
 * 
 */
public class DueDateCalculator {
	// TODO this class can be improved avoiding global variables and better
	// separating responsibilities

	private List<CalculatorBlock> currentDateCalendarBlocks;

	private List<CalculatorBlock> unmergedCalendarBlocks;

	private long currentDateOnDurationInMinutes;

	private LocalDateTime currentDateStartDateTime;

	private long slaCounterInMinutes = 0;

	private OverlapCalendarRule overlapCalendarRule;

	public DueDateCalculator() {
		this.overlapCalendarRule = OverlapCalendarRule.builder().build();
	}

	/**
	 * Returns {@link CalculationLog} holding the Due Date and the calculation
	 * history log with a list of {@link CalculationLogBlock}. The history log
	 * structure represents each of the calendars blocks with work time available to
	 * be consumed by the SLA.
	 * 
	 * @param calendar
	 * @param startDateTime
	 * @param slaInMinutes
	 * @return
	 */
	public CalculationLog calculateDueDateWithLog(Calendar calendar, LocalDateTime startDateTime, long slaInMinutes) {
		CalculationLog calculationLog = new CalculationLog();

		LocalDateTime dueDateTime = calculateDueDate(calendar, startDateTime, slaInMinutes, calculationLog);

		if (calculationLog != null && !calculationLog.isEmpty()) {
			calculationLog.setStartDateTime(startDateTime);
			calculationLog.setDueDateTime(dueDateTime);
			CalculationLogTimeTruncator.builder().build().visit(calculationLog);
		}

		return calculationLog;

	}

	/**
	 * Returns {@link CalculationLog} holding only the Due Date but not the
	 * calculation history log. Should be used for best performance and/or the
	 * calculation history log is not needed
	 * 
	 * @param calendar
	 * @param startDateTime
	 * @param slaInMinutes
	 * @return
	 */
	public CalculationLog calculateDueDate(Calendar calendar, LocalDateTime startDateTime, long slaInMinutes) {
		return new CalculationLog(calculateDueDate(calendar, startDateTime, slaInMinutes, null));
	}

	private LocalDateTime calculateDueDate(Calendar calendar, LocalDateTime startDateTime, long slaInMinutes,
			CalculationLog calculationLog) {

		initCalendarBlocks(calendar, startDateTime, calculationLog);
		if (slaInMinutes == 0) {
			return startDateTime;
		}

		this.slaCounterInMinutes = slaInMinutes;
		this.currentDateStartDateTime = startDateTime;

		advanceToDueDateDay(calendar, slaInMinutes, calculationLog);

		CalculatorBlock calendarBlock = getDueDateCalendarBlockAndUpdateSLACounter();
		LocalDateTime dueDateTime = addMinutes(Long.valueOf(slaCounterInMinutes).intValue(), calendarBlock.getEnd());

		return dueDateTime;
	}

	/*
	 * Advance to the due date block
	 */
	private CalculatorBlock getDueDateCalendarBlockAndUpdateSLACounter() {
		Iterator<CalculatorBlock> calendarBlockIterator = this.currentDateCalendarBlocks.iterator();
		CalculatorBlock calendarBlock = null;
		while (slaCounterInMinutes > 0) {
			calendarBlock = calendarBlockIterator.next();

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

	/*
	 * Advance to the due date day
	 */
	private void advanceToDueDateDay(Calendar calendar, long slaInMinutes, CalculationLog calculationLog) {
		long rollingSlaMinutes = getRollingSlaMinutes(currentDateStartDateTime, slaInMinutes);
		while (rollingSlaMinutes > 0) {
			nextCalendarBlocksDay(calendar, calculationLog);
			if (rollingSlaMinutes >= 0) {
				slaCounterInMinutes = rollingSlaMinutes;
				currentDateStartDateTime = this.currentDateCalendarBlocks.get(0).getStart();
			}
			rollingSlaMinutes = getRollingSlaMinutes(currentDateStartDateTime, rollingSlaMinutes);
		}
	}

	/*
	 * Initialize calendar blocks at first day
	 */
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

			this.currentDateCalendarBlocks.add(calendarBlock);

			calendarBlock.accept(DayLightSavingVisitor.builder()
					.dayLightSavingInfoByLocation(calendar.getDayLightSavingInfoByLocation()).build());

			calendarBlock.accept(NonBusinessDayVisitor.builder()
					.nonBusinessDaysByLocation(calendar.getNonBusinessDaysByLocation()).build());
		}

		unmergedCalendarBlocks = new ArrayList<>(currentDateCalendarBlocks);
		currentDateCalendarBlocks = overlapCalendarRule.applyTo(currentDateCalendarBlocks);

		buildCalculationLog(calculationLog);

		updateOnDurationInMinutes();
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

	private void nextCalendarBlocksDay(Calendar calendar, CalculationLog calculationLog) {

		unmergedCalendarBlocks.forEach(o -> o.nextDay());

		unmergedCalendarBlocks.forEach(o -> o.accept(DayLightSavingVisitor.builder()
				.dayLightSavingInfoByLocation(calendar.getDayLightSavingInfoByLocation()).build()));

		unmergedCalendarBlocks.forEach(o -> o.accept(NonBusinessDayVisitor.builder()
				.nonBusinessDaysByLocation(calendar.getNonBusinessDaysByLocation()).build()));

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
