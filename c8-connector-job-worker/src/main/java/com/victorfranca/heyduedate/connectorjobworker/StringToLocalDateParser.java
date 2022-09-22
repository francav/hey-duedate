package com.victorfranca.heyduedate.connectorjobworker;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author victor.franca
 *
 */
public class StringToLocalDateParser {

	public static LocalDateTime extractLocalDateTime(String startDate) {
		return LocalDateTime.parse(startDate.replaceAll("Z\\[Etc/UTC\\]", "")).truncatedTo(ChronoUnit.MINUTES);
	}

}
