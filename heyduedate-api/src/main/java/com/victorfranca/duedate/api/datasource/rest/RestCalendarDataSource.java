package com.victorfranca.duedate.api.datasource.rest;

import java.util.Collections;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.victorfranca.duedate.api.datasource.CalendarDataSource;
import com.victorfranca.duedate.api.datasource.CalendarDataSourceException;
import com.victorfranca.duedate.calendar.Calendar;
import com.victorfranca.duedate.calendar.provider.json.JSONCalendarProvider;
import com.victorfranca.duedate.calendar.provider.spi.exception.CalendarElementNotFound;
import com.victorfranca.duedate.calendar.provider.spi.exception.InvalidCalendarException;

@Component("restCalendarDS")
public class RestCalendarDataSource implements CalendarDataSource {

	private String url;

	private MultiValueMap<String, String> headers;

	public RestCalendarDataSource(@Value("${calendar-datasource-rest.url}") String url) {
		this.url = url;
	}

	@Override
	// TODO include calendar in the request URL
	public Calendar getCalendarData(String calendar) throws CalendarDataSourceException {
		initHttpHeaderMap();

		try {
			return new JSONCalendarProvider(executeGetRequest(url).getBody()).createCalendar();
		} catch (CalendarElementNotFound | InvalidCalendarException e) {
			throw new CalendarDataSourceException(e.getMessage(), e);
		}
	}

	private ResponseEntity<JSONObject> executeGetRequest(String resourceUrl) {

		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());

		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));

		return restTemplate.exchange(resourceUrl, HttpMethod.GET, new HttpEntity<Object>(headers), JSONObject.class);
	}

	private void initHttpHeaderMap() {

		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));

		this.headers = new LinkedMultiValueMap<>();
		this.headers.add("Content-Type", "application/json");
		this.headers.add("Accept", "application/json");

	}

	@Override
	public List<String> getCalendars() throws CalendarDataSourceException {
		// TODO Auto-generated method stub
		return null;
	}

}
