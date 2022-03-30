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
package com.victorfranca.duedate.api.datasource.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import com.victorfranca.duedate.api.datasource.CalendarDataSource;
import com.victorfranca.duedate.api.datasource.CalendarDataSourceException;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.provider.json.JSONCalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.exception.CalendarElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.exception.InvalidCalendarException;

@Component("fileCalendarDS")
public class FileCalendarDataSource implements CalendarDataSource {

	private static final String JSON_EXTENSION = ".json";

	@Value("${calendar-datasource-folder.name}")
	private String resourceFolderName;

	@Autowired
	private ResourceLoader resourceLoader;
	
	public Calendar getCalendarData(String calendarFileName) throws CalendarDataSourceException {
		JSONParser jsonParser = new JSONParser();

		try {

			InputStream resourceInputStream = resourceLoader
					.getResource("classpath:" + resourceFolderName + calendarFileName + JSON_EXTENSION)
					.getInputStream();

			// TODO replace constructor invokation JSONCalendarProvider ?
			return new JSONCalendarProvider((JSONObject) jsonParser.parse(new InputStreamReader(resourceInputStream)))
					.createCalendar();
		} catch (IOException | ParseException | CalendarElementNotFound | InvalidCalendarException e) {
			throw new CalendarDataSourceException(e.getMessage(), e);
		}

	}

	@Override
	public List<String> getCalendars() throws CalendarDataSourceException {
		try {
			Resource[] calendarsFiles = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
					.getResources("classpath:" + resourceFolderName + "*" + JSON_EXTENSION);
			return Arrays.asList(calendarsFiles).stream().map(o -> o.getFilename().replaceAll(JSON_EXTENSION, ""))
					.collect(Collectors.toList());

		} catch (IOException e) {
			throw new CalendarDataSourceException(e.getMessage(), e);
		}
	}

}
