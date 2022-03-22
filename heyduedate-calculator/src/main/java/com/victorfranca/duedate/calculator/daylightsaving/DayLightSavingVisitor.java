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
package com.victorfranca.duedate.calculator.daylightsaving;

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
public class DayLightSavingVisitor implements CalculatorBlockVisitor {

	private Map<String, List<DayLightSavingInfo>> dayLightSavingInfoByLocation;

	public static DayLightSavingVisitorBuilder builder(
			Map<String, List<DayLightSavingInfo>> dayLightSavingInfoByLocation) {
		return hiddenBuilder().dayLightSavingInfoByLocation(dayLightSavingInfoByLocation);
	}

	@Override
	public void visit(CalculatorBlock calendarBlock) {

		if (dayLightSavingInfoByLocation != null) {
			if (dayLightSavingInfoByLocation.containsKey(calendarBlock.getLocationId())) {

				List<DayLightSavingInfo> dayLightSavingInfoList = dayLightSavingInfoByLocation
						.get(calendarBlock.getLocationId());

				for (DayLightSavingInfo dayLightSavingInfo : dayLightSavingInfoList) {

					updateStartDateForDST(calendarBlock, dayLightSavingInfo);

					updateEndDateForDST(calendarBlock, dayLightSavingInfo);
				}
			}
		}

	}

	private void updateStartDateForDST(CalculatorBlock calendarBlock, DayLightSavingInfo dayLightSavingInfo) {
		if (Dates.isBetween(dayLightSavingInfo.getStart(), dayLightSavingInfo.getEnd(), calendarBlock.getStart())) {
			calendarBlock
					.setStart(Dates.addMinutes(1 * dayLightSavingInfo.getOffsetInMinutes(), calendarBlock.getStart()));
			calendarBlock.setDstAffected(true);
		} else {
			calendarBlock.setDstAffected(false);
		}
	}

	private void updateEndDateForDST(CalculatorBlock calendarBlock, DayLightSavingInfo dayLightSavingInfo) {
		if (Dates.isBetween(dayLightSavingInfo.getStart(), dayLightSavingInfo.getEnd(), calendarBlock.getEnd())) {
			calendarBlock.setEnd(Dates.addMinutes(1 * dayLightSavingInfo.getOffsetInMinutes(), calendarBlock.getEnd()));
			calendarBlock.setDstAffected(true);
		} else {
			calendarBlock.setDstAffected(false);
		}
	}

}
