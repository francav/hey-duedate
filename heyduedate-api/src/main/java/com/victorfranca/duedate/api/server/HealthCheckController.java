package com.victorfranca.duedate.api.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthCheckController {

	@GetMapping("/")
	public String healthCheck() {
		return "Health Check Ok!";
	}

}
