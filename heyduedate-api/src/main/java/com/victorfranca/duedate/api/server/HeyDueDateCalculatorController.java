/*
 * Copyright WKS Power Limited under one or more contributor license agreements. 
 * See the LICENSE file distributed with this work for additional information regarding 
 * copyright ownership. WKS Power licenses this file to you under the 
 * GNU AFFERO GENERAL PUBLIC LICENSE v3.0; you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/agpl-3.0.en.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.victorfranca.duedate.api.server;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.annotation.Resource;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calculator.log.CalculationLog;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.datasource.CalendarDataSource;
import com.victorfranca.duedate.calendar.datasource.CalendarDataSourceException;

@RestController
class HeyDueDateCalculatorController {

	// TODO adapt to noums and verbs REST structure

	@Resource(name = "calendarDataSource")
	private CalendarDataSource calendarDataSource;

	@GetMapping(value = "/duedate")
	public CalculationLog getDueDateWithLog(@RequestParam("calendar") String calendarName,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
			@RequestParam("sla") Integer slaInMinutes, @RequestParam("log") Boolean logEnabled) {

		startDateTime = startDateTime.truncatedTo(ChronoUnit.MINUTES);

		Calendar calendar = null;
		try {
			calendar = calendarDataSource.getCalendarData(calendarName);
		} catch (CalendarDataSourceException e) {
			throw new RuntimeException("Internal Error", e);
		}

		if (logEnabled) {
			return new DueDateCalculator().calculateDueDateWithLog(calendar, startDateTime, slaInMinutes);
		} else {
			return new DueDateCalculator().calculateDueDate(calendar, startDateTime, slaInMinutes);
		}

	}

}
