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
package com.victorfranca.duedate.calculator.log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

	public void add(CalculationLogBlock calculationLogBlock) {
		calculationLogBlocks.add(calculationLogBlock);
	}

	public boolean isEmpty() {
		return calculationLogBlocks.isEmpty();
	}

	public CalculationLogBlock get(int index) {
		return calculationLogBlocks.get(index);
	}

	public void accept(CalculationLogVisitor calculationLogVisitor) {
		calculationLogVisitor.visit(this);
	}

}
