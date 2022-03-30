package com.victorfranca.duedate.calculator.log;

import java.time.LocalDateTime;

import com.victorfranca.duedate.calculator.Dates;

import lombok.Builder;

@Builder
public class CalculationLogTimeTruncator implements CalculationLogVisitor {

	// TODO improve performance
	public void visit(CalculationLog calculationLog) {

		for (CalculationLogBlock calculationLogBlock : calculationLog.getCalculationLogBlocks()) {

			LocalDateTime startDateTime = calculationLog.getStartDateTime();
			LocalDateTime dueDateTime = calculationLog.getDueDateTime();

			if (!calculationLogBlock.isOn()) {
				calculationLogBlock.setSlaUsedTimeInMinutes(0l);
			} else if (startDateTime.isAfter(calculationLogBlock.getStart())
					&& dueDateTime.isBefore(calculationLogBlock.getEnd())) {
				calculationLogBlock.setSlaUsedTimeInMinutes(Dates.diffInMinutes(dueDateTime, startDateTime));
			} else if (dueDateTime.isBefore(calculationLogBlock.getStart())) {
				calculationLogBlock.setSlaUsedTimeInMinutes(0l);
			} else if (calculationLogBlock.getStart().isBefore(startDateTime)
					&& calculationLogBlock.getEnd().isBefore(startDateTime)) {
				calculationLogBlock.setSlaUsedTimeInMinutes(0l);
			} else if (startDateTime.isAfter(calculationLogBlock.getStart())
					&& !startDateTime.isAfter(calculationLogBlock.getEnd())) {
				calculationLogBlock
						.setSlaUsedTimeInMinutes(Dates.diffInMinutes(calculationLogBlock.getEnd(), startDateTime));
			} else if (dueDateTime.isBefore(calculationLogBlock.getEnd())) {
				calculationLogBlock
						.setSlaUsedTimeInMinutes(Dates.diffInMinutes(dueDateTime, calculationLogBlock.getStart()));
			}
		}
	}

}
