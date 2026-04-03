package ro.stefan.gateway.controller;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.stefan.gateway.dto.GatewayHealthResponse;

@RestController
public class HealthController {
	
	@GetMapping("/internal/health")
	public GatewayHealthResponse health() {
		return new GatewayHealthResponse("api-gateway", "UP", Instant.now().toString());
	}
}
