package com.victorfranca.duedate.calculator.overlap;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import com.victorfranca.duedate.calculator.CalculatorBlock;

public class OverlapCalendarRuleTest {

	@Test
	public void applyToTest() {

		OverlapCalendarRule overlapCalendarMerger = OverlapCalendarRule.builder().build();

		CalculatorBlock calculatorBlock1 = new CalculatorBlock("location1", LocalDateTime.of(2022, 1, 1, 0, 0),
				LocalDateTime.of(2022, 1, 1, 5, 0));

		CalculatorBlock calculatorBlock2 = new CalculatorBlock("location2", LocalDateTime.of(2022, 1, 1, 3, 0),
				LocalDateTime.of(2022, 1, 1, 7, 0));

		List<CalculatorBlock> calculatorBlocks = List.of(calculatorBlock1, calculatorBlock2);

		List<CalculatorBlock> mergedBlocks = overlapCalendarMerger.applyTo(calculatorBlocks);

		assertEquals(LocalDateTime.of(2022, 1, 1, 0, 0), mergedBlocks.get(0).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 3, 0), mergedBlocks.get(0).getEnd());

		assertEquals(LocalDateTime.of(2022, 1, 1, 3, 0), mergedBlocks.get(1).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 5, 0), mergedBlocks.get(1).getEnd());

		assertEquals(LocalDateTime.of(2022, 1, 1, 5, 0), mergedBlocks.get(2).getStart());
		assertEquals(LocalDateTime.of(2022, 1, 1, 7, 0), mergedBlocks.get(2).getEnd());
	}

}
