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
package com.victorfranca.duedate.calculator;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * @author victor.franca
 *
 */
@Getter
@Setter
public class CalculatorBlock {

	private String locationId;

	private LocalDateTime originalStart;
	private LocalDateTime originalEnd;

	private LocalDateTime start;
	private LocalDateTime end;
	private boolean on = true;

	private long durationInMinutes;

	private boolean dstAffected;

	public CalculatorBlock(String locationId, LocalDateTime start, LocalDateTime end) {
		this.locationId = locationId;
		this.start = start;
		this.end = end;
		this.originalStart = start;
		this.originalEnd = end;
		updateDurationInMinutes();
	}

	public void updateDurationInMinutes() {
		this.durationInMinutes = Dates.diffInMinutes(getEnd(), getStart());
	}

	public void nextDay() {
		setStart(Dates.addDays(1, getOriginalStart()));
		setEnd(Dates.addDays(1, getOriginalEnd()));

		setOriginalStart(start);
		setOriginalEnd(end);
	}

	public boolean isOn() {
		return on;
	}

	public void accept(CalculatorBlockVisitor nonBusinessDaysVisitor) {
		nonBusinessDaysVisitor.visit(this);
	}

}
