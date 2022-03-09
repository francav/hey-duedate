package com.victorfranca.duedate.calendar.nonbusinesshour;

public class NonBusinessDayVisitorFactory {
	
	private NonBusinessDayVisitorFactory() {
	}

	public static NonBusinessDayVisitor getNonBusinessDayVisitor(
			NonBusinessDayProviderFactory nonBusinessDaysProviderFactory) {
		return new NonBusinessDayVisitor(nonBusinessDaysProviderFactory);
	}

}
