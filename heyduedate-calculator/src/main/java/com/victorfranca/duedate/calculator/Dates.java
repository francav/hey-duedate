/*
 * Copyright WKS Power Limited under one or more contributor license agreements. 
 * See the LICENSE file distributed with this work for additional information regarding 
 * copyright ownership. WKS Power licenses this file to you under the 
 * GNU General Public License v3.0; you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.victorfranca.duedate.calculator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author victor.franca
 *
 */
public class Dates {

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
