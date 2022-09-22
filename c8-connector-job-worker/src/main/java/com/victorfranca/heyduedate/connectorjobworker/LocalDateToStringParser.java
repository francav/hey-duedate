package com.victorfranca.heyduedate.connectorjobworker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author victor.franca
 *
 */
public class LocalDateToStringParser {

	public static String parseToString(LocalDateTime startDate) {
		return DateTimeFormatter.ISO_DATE_TIME.format(startDate) + "Z";
	}

}
