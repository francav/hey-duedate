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
package com.victorfranca.duedate.calculator.singleday.singleblock;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.victorfranca.duedate.calculator.DueDateCalculator;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.LocationRegularBusinessHours;

/**
 * @author victor.franca
 *
 */
public class SingleBlock_StartDateInOrAfterFirstCalendarBlock_Test {

	private Calendar calendar;
	private DueDateCalculator dueDateCalculator;

	private static final int START_HOUR_1 = 3;
	private static final int END_HOUR_1 = 6;

	private static final String LOCATION_ID_1 = "LOCATION_ID_1";

	@Before
	public void init() {
		// Given
		calendar = new Calendar();
		dueDateCalculator = new DueDateCalculator();

		calendar.setRegularBusinessHours(List.of(LocationRegularBusinessHours.builder().location(LOCATION_ID_1)
				.startHour(START_HOUR_1).startMinute(0).endHour(END_HOUR_1).endMinute(0).build()));

	}

	//////////////////////////////////////
	// SLA: 240 MINUTES
	/////////////////////////////////////

	@Test
	public void calculateDueDateTest_16_00_4h() {
		// When
		int slaInMinutes = 60 * 4;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 16, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 3, 4, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());

	}

	//////////////////////////////////////
	// SLA: 60 MINUTES
	/////////////////////////////////////

	@Test
	public void calculateDueDateTest_03_00_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());

	}

	@Test
	public void calculateDueDateTest_03_01_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 01);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 01),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());

	}

	@Test
	public void calculateDueDateTest_01_59_1h() {
		// When
		int slaInMinutes = 60 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 59);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 59),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	//////////////////////////////////////
	// SLA: 1 MINUTE
	/////////////////////////////////////

	@Test
	public void calculateDueDateTest_03_00_01m() {
		// When
		int slaInMinutes = 1 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 00);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 3, 01),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

	@Test
	public void calculateDueDateTest_03_01_01m() {
		// When
		int slaInMinutes = 1 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 01);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 3, 02),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());

	}

	@Test
	public void calculateDueDateTest_01_59_01m() {
		// When
		int slaInMinutes = 1 * 1;
		LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 3, 59);

		// Then
		assertEquals(LocalDateTime.of(2022, 1, 1, 4, 00),
				dueDateCalculator.calculateDueDate(calendar, startDateTime, slaInMinutes).getDueDateTime());
	}

}
