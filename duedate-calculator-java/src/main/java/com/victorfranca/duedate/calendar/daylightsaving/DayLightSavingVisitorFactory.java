package com.victorfranca.duedate.calendar.daylightsaving;

public class DayLightSavingVisitorFactory {

	private DayLightSavingVisitorFactory() {
	}

	public static DayLightSavingVisitor getDayLightSavingVisitor(
			DayLightSavingProviderFactory daylightSavingProviderFactory) {
		return new DayLightSavingVisitor(daylightSavingProviderFactory);
	}

}
