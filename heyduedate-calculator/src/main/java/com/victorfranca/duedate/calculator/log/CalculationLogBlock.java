package com.victorfranca.duedate.calculator.log;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalculationLogBlock {

	private LocalDateTime start;
	private LocalDateTime end;

	public Long slaUsedTimeInMinutes;

	public boolean on;
	public boolean dstAffected;

}
