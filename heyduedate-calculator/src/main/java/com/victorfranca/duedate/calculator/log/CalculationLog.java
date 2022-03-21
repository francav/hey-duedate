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
package com.victorfranca.duedate.calculator.log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.victorfranca.duedate.calculator.Dates;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CalculationLog {

	private List<CalculationLogBlock> calculationLogBlocks = new ArrayList<CalculationLogBlock>();

	private LocalDateTime startDateTime;
	private LocalDateTime dueDateTime;

	public CalculationLog(LocalDateTime dueDateTime) {
		this.dueDateTime = dueDateTime;
	}

	// TODO improve for performance
	// TODO maybe a visitor?
	// TODO what about non-business hours and DST info?
	public void truncateTimeUsedBySLA() {

		for (CalculationLogBlock calculationLogBlock : calculationLogBlocks) {
			if (!calculationLogBlock.isOn()) {
				calculationLogBlock.setSlaUsedTimeInMinutes(0l);
			} else if (startDateTime.isAfter(calculationLogBlock.getStart())
					&& dueDateTime.isBefore(calculationLogBlock.getEnd())) {
				calculationLogBlock.setSlaUsedTimeInMinutes(Dates.diffInMinutes(dueDateTime, startDateTime));
			} else if (dueDateTime.isBefore(calculationLogBlock.getStart())) {
				calculationLogBlock.setSlaUsedTimeInMinutes(0l);
			} else if (calculationLogBlock.getStart().isBefore(startDateTime)
					&& calculationLogBlock.getEnd().isBefore(startDateTime)) {
				calculationLogBlock.setSlaUsedTimeInMinutes(0l);
			} else if (startDateTime.isAfter(calculationLogBlock.getStart())
					&& !startDateTime.isAfter(calculationLogBlock.getEnd())) {
				calculationLogBlock
						.setSlaUsedTimeInMinutes(Dates.diffInMinutes(calculationLogBlock.getEnd(), startDateTime));
			} else if (dueDateTime.isBefore(calculationLogBlock.getEnd())) {
				calculationLogBlock
						.setSlaUsedTimeInMinutes(Dates.diffInMinutes(dueDateTime, calculationLogBlock.getStart()));
			}
		}
	}

	public void add(CalculationLogBlock calculationLogBlock) {
		calculationLogBlocks.add(calculationLogBlock);
	}

	public boolean isEmpty() {
		return calculationLogBlocks.isEmpty();
	}

	public CalculationLogBlock get(int index) {
		return calculationLogBlocks.get(index);
	}

}
