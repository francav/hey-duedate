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
package com.victorfranca.duedate.calculator.nonbusinesshour;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calculator.CalculatorBlock;
import com.victorfranca.duedate.calculator.CalculatorBlockVisitor;
import com.victorfranca.duedate.calculator.Dates;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * @author victor.franca
 *
 */
@AllArgsConstructor
@Builder(builderMethodName = "hiddenBuilder")
public class NonBusinessDayVisitor implements CalculatorBlockVisitor {

	private Map<String, List<LocalDate>> nonBusinessDaysByLocation;

	public static NonBusinessDayVisitorBuilder builder(Map<String, List<LocalDate>> nonBusinessDaysByLocation) {
		return hiddenBuilder().nonBusinessDaysByLocation(nonBusinessDaysByLocation);
	}

	@Override
	public void visit(CalculatorBlock calendarBlock) {
		calendarBlock.setOn(!isNonBusinessHour(calendarBlock));
	}

	// TODO DST: comparing to calendar block start date may not work since end date
	// may fall in next day (unit test this)
	private boolean isNonBusinessHour(CalculatorBlock calendarBlock) {

		if (nonBusinessDaysByLocation == null) {
			return false;
		} else {

			if (!nonBusinessDaysByLocation.containsKey(calendarBlock.getLocationId())) {
				return false;
			}

			return nonBusinessDaysByLocation.get(calendarBlock.getLocationId()).stream()
					.anyMatch(o -> Dates.isSameDay(o, calendarBlock.getStart().toLocalDate()));
		}
	}

}
