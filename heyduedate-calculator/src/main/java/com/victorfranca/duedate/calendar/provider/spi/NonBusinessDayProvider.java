package com.victorfranca.duedate.calendar.provider.spi;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author victor.franca
 *
 */
public interface NonBusinessDayProvider {

	public Map<String, List<LocalDate>> getNonBusinessDaysByLocation();

}
