package com.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//TODO replace by module 
public class DateUtil {

	public static LocalDateTime addMinutes(int minutes, LocalDateTime date) {
		return date.plusMinutes(minutes);
	}

	public static LocalDateTime addDays(int day, LocalDateTime date) {
		return date.plusDays(day);
	}

	public static long diffInMinutes(LocalDateTime toDate, LocalDateTime fromDate) {
		return ChronoUnit.MINUTES.between(fromDate, toDate);
	}

	public static boolean isSameDay(LocalDate date1, LocalDate date2) {
		return date1.isEqual(date2);
	}

	public static boolean isBetween(LocalDateTime min, LocalDateTime max, LocalDateTime date) {
		return !date.isBefore(min) && !date.isAfter(max);
	}

}
