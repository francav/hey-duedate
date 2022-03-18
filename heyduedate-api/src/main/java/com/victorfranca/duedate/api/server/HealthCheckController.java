package com.victorfranca.duedate.api.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
class HealthCheckController {

	@GetMapping("/")
	public String calendars() {
		return "Health Check Ok!";
	}

}
