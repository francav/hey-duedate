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
package com.victorfranca.duedate.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calculator.daylightsaving.DayLightSavingInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author victor.franca
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class Calendar {

	private List<LocationRegularBusinessHours> regularBusinessHours = new ArrayList<>();

	private Map<String, List<LocalDate>> nonBusinessDaysByLocation = new LinkedHashMap<>();

	private Map<String, List<DayLightSavingInfo>> dayLightSavingInfoByLocation = new LinkedHashMap<>();

	public void addLocationRegularBusinessHours(LocationRegularBusinessHours locationRegularBusinessHours) {
		this.regularBusinessHours.add(locationRegularBusinessHours);
	}

	public void addNonBusinessDaysByLocation(String location, List<LocalDate> nonBusinessDays) {
		this.nonBusinessDaysByLocation.put(location, nonBusinessDays);
	}

	public void addDayLightSavingInfoByLocation(String location,
			List<DayLightSavingInfo> dayLightSavingInfoByLocation) {
		this.dayLightSavingInfoByLocation.put(location, dayLightSavingInfoByLocation);
	}

}
