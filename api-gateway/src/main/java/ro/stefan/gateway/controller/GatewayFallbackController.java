package ro.stefan.gateway.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class GatewayFallbackController {

	@RequestMapping(value = "fallback", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Map<String, Object>> fallback(){
		return Mono.just(Map.of(
					"timestamp", Instant.now().toString(),
					"status", HttpStatus.SERVICE_UNAVAILABLE.value(),
					"errorCode", "DOWNSTREAM_SERVICE_UNAVAILABLE",
					"message", "Downstream service is unavailable"
				));
	}
}
