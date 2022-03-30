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
package com.victorfranca.duedate.calendar;

import java.time.LocalDate;
import java.util.Map;

import com.victorfranca.duedate.calculator.partialbusinesshours.PartialBusinessHour;

import lombok.Builder;

/**
 * @author victor.franca
 *
 */
@Builder
public class LocationRegularBusinessHours {

	private String location;

	@Builder.Default
	private int startHour = -1;

	@Builder.Default
	private int startMinute = -1;

	@Builder.Default
	private int endHour = -1;

	@Builder.Default
	private int endMinute = -1;

	private Map<LocalDate, PartialBusinessHour> partialBusinessHoursMap;

	public int getStartHour(LocalDate date) {
		if (partialBusinessHoursMap == null || partialBusinessHoursMap.isEmpty()
				|| partialBusinessHoursMap.get(date) == null) {
			return startHour;
		}

		return partialBusinessHoursMap.get(date).getStartHour();
	}

	public int getStartMinute(LocalDate date) {
		if (partialBusinessHoursMap == null || partialBusinessHoursMap.isEmpty()
				|| partialBusinessHoursMap.get(date) == null) {
			return startMinute;
		}

		return partialBusinessHoursMap.get(date).getStartMinute();
	}

	public int getEndHour(LocalDate date) {
		if (partialBusinessHoursMap == null || partialBusinessHoursMap.isEmpty()
				|| partialBusinessHoursMap.get(date) == null) {
			return endHour;
		}

		return partialBusinessHoursMap.get(date).getEndHour();
	}

	public int getEndMinute(LocalDate date) {
		if (partialBusinessHoursMap == null || partialBusinessHoursMap.isEmpty()
				|| partialBusinessHoursMap.get(date) == null) {
			return endMinute;
		}

		return partialBusinessHoursMap.get(date).getEndMinute();
	}
	
	public Map<LocalDate, PartialBusinessHour> getPartialBusinessHoursMap() {
		return partialBusinessHoursMap;
	}

	public String getLocation(LocalDate date) {
		return location;
	}
}
