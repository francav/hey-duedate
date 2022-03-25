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
package com.victorfranca.duedate.calculator.overlap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.victorfranca.duedate.calculator.CalculatorBlock;

import lombok.Builder;

@Builder
public class OverlapCalendarRule {

	public List<CalculatorBlock> applyTo(List<CalculatorBlock> blocks) {

		List<CalculatorBlock> mergedBlocks = new ArrayList<>(blocks);

		Map<Integer, CalculatorBlock> newBlocks = new LinkedHashMap<>();

		for (int i = 1; i < blocks.size(); i++) {
			CalculatorBlock currBlock = blocks.get(i);

			CalculatorBlock prevBlock = blocks.get(i - 1);
			if (prevBlock.isOn() && currBlock.isOn() && prevBlock.getEnd().isAfter(currBlock.getStart())) {
				// creates new block
				CalculatorBlock newBlock = new CalculatorBlock(
						getMergedLocationId(currBlock, prevBlock, currBlock.isDstAffected(), prevBlock.isDstAffected()),
						currBlock.getStart(), prevBlock.getEnd());
				newBlocks.put(i, newBlock);

				// truncate previous block
				LocalDateTime prevEnd = prevBlock.getEnd();
				prevBlock.setEnd(currBlock.getStart());
				prevBlock.updateDurationInMinutes();

				// truncate current block
				currBlock.setStart(prevEnd);
				currBlock.updateDurationInMinutes();
			}
		}

		newBlocks.forEach((k, v) -> mergedBlocks.add(k, v));

		return mergedBlocks;

	}

	//TODO should really add DST info here? Out of pattern
	private String getMergedLocationId(CalculatorBlock currBlock, CalculatorBlock prevBlock, boolean currDSTAffected,
			boolean prevDSTAffected) {
		return prevBlock.getLocationId() + (prevDSTAffected ? "(DST)" : "") + "/" + currBlock.getLocationId()
				+ (currDSTAffected ? "(DST)" : "");
	}

}
