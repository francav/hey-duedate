package com.victorfranca.duedate.calculator.log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.victorfranca.duedate.calculator.Dates;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CalculationLog {

	private List<CalculationLogBlock> calculationLogBlocks = new ArrayList<CalculationLogBlock>();

	private LocalDateTime startDateTime;
	private LocalDateTime dueDateTime;

	// TODO improve for performance
	// TODO maybe a visitor?
	// TODO what about non-business hours and DST info?
	public void truncateTimeUsedBySLA() {

		for (CalculationLogBlock calculationLogBlock : calculationLogBlocks) {
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

	public void add(CalculationLogBlock calculationLogBlock) {
		calculationLogBlocks.add(calculationLogBlock);
	}

	public boolean isEmpty() {
		return calculationLogBlocks.isEmpty();
	}

	public CalculationLogBlock get(int index) {
		return calculationLogBlocks.get(index);
	}

}
