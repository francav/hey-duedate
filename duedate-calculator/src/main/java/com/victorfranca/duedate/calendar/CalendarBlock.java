package com.victorfranca.duedate.calendar;

import java.time.LocalDateTime;

import com.victorfranca.duedate.Dates;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarBlock {

	private String locationId;

	private LocalDateTime start;
	private LocalDateTime end;
	private boolean on = true;

	private long durationInMinutes;

	public CalendarBlock(String locationId, LocalDateTime start, LocalDateTime end) {
		this.locationId = locationId;
		this.start = start;
		this.end = end;
		this.durationInMinutes = Dates.diffInMinutes(getEnd(), getStart());
	}

	public void nextDay() {
		setStart(Dates.addDays(1, getStart()));
		setEnd(Dates.addDays(1, getEnd()));
	}

	public boolean isOn() {
		return on;
	}

	public void accept(CalendarBlockVisitor nonBusinessDaysVisitor) {
		nonBusinessDaysVisitor.visit(this);
	}

}
