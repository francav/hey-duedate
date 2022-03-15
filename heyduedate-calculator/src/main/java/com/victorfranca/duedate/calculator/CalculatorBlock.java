package com.victorfranca.duedate.calculator;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * @author victor.franca
 *
 */
@Getter
@Setter
public class CalculatorBlock {

	private String locationId;

	private LocalDateTime originalStart;
	private LocalDateTime originalEnd;

	private LocalDateTime start;
	private LocalDateTime end;
	private boolean on = true;

	private long durationInMinutes;

	private boolean dstAffected;

	public CalculatorBlock(String locationId, LocalDateTime start, LocalDateTime end) {
		this.locationId = locationId;
		this.start = start;
		this.end = end;
		this.originalStart = start;
		this.originalEnd = end;
		updateDurationInMinutes();
	}

	public void updateDurationInMinutes() {
		this.durationInMinutes = Dates.diffInMinutes(getEnd(), getStart());
	}

	public void nextDay() {
		setStart(Dates.addDays(1, getOriginalStart()));
		setEnd(Dates.addDays(1, getOriginalEnd()));
		
		setOriginalStart(start);
		setOriginalEnd(end);
	}

	public boolean isOn() {
		return on;
	}

	public void accept(CalculatorBlockVisitor nonBusinessDaysVisitor) {
		nonBusinessDaysVisitor.visit(this);
	}

}
